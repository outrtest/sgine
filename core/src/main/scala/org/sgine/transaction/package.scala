package org.sgine

import java.lang.{RuntimeException, ThreadLocal}

package object transaction {
  private val local = new ThreadLocal[Transaction]

  /**
   * Creates a transaction around the function supplied rolling back if an exception is thrown and committing if
   * completed successfully. Transactions revolve around classes that mix-in the Transactable trait.
   *
   * Transactions are local to the thread that are executed in.
   */
  def transaction(f: => Unit) = {
    val t = new Transaction()
    try {
      local.set(t)
      f
      local.set(null)
      t.commit()
    } catch {
      case throwable => {
        t.rollback()
        throw throwable
      }
    } finally {
      local.set(null)
    }
  }

  /**
   * Returns true if currently within a transaction.
   */
  def isTransaction = local.get != null

  /**
   * Retrieves the current value of the passed Transactable.
   *
   * NOTE: A RuntimeException is thrown if this method is invoked outside of a transaction.
   *
   * @return T
   */
  def current[T](transactable: Transactable[T]): T = local.get() match {
    case null => throw new RuntimeException("Not currently in a transaction")
    case transaction => transaction.map.get(transactable).map(state2current).getOrElse(null).asInstanceOf[T]
  }

  /**
   * Sets the current value of the passed Transactable.
   *
   * NOTE: A RuntimeException is thrown if this method is invoked outside of a transaction.
   */
  def current[T](transactable: Transactable[T], value: T) = local.get() match {
    case null => throw new RuntimeException("Not currently in a transaction")
    case transaction => {
      val state = if (transaction.map.contains(transactable)) {
        val state = transaction.map(transactable).asInstanceOf[TransactionState[T]]
        state.newState(value)
      } else {
        TransactionState(transactable, transactable.value, value)
      }
      transaction.map += transactable -> state
    }
  }

  private val state2current = (ts: TransactionState[_]) => ts.current
}