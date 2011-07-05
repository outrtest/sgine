package org.sgine

import java.lang.{RuntimeException, ThreadLocal}

package object transaction {
  private val local = new ThreadLocal[Transaction]

  def transaction(f: => Unit) = {
    try {
      local.set(new Transaction())
      f
      local.get().commit()
    } catch {
      case throwable => {
        local.get().rollback()
        throw throwable
      }
    } finally {
      local.set(null)
    }
  }

  def isTransaction = local.get != null

  def current[T](transactable: Transactable[T]): T = local.get() match {
    case null => throw new RuntimeException("Not currently in a transaction")
    case transaction => transaction.map.get(transactable).map(state2current).getOrElse(null).asInstanceOf[T]
  }

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