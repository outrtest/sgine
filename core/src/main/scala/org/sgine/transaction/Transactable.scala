/*
 * Copyright (c) 2011 Sgine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 *  Neither the name of 'Sgine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sgine.transaction

/**
 * Transactable mix-in provides support for transactions.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Transactable[T] {
  /**
   * Convenience method pointing to transaction.isTransaction
   *
   * @see org.sgine.transaction.isTransaction
   */
  final def isTransaction = org.sgine.transaction.isTransaction

  /**
   * Retrieves the current value of this Transactable
   */
  protected[transaction] def value: T

  /**
   * Sets the transaction value for this Transactable.
   *
   * @throws RuntimeException if not in transaction.
   */
  protected[transaction] def transactionValue_=(value: T) = org.sgine.transaction.current(this, value)

  /**
   * Gets the transaction value for this Transactable.
   *
   * @throws RuntimeException if not in transaction.
   */
  protected[transaction] def transactionValue: T = org.sgine.transaction.current(this)

  /**
   * Commits the supplied value to this Transactable. This will be called by the transaction and should never be
   * invoked manually.
   */
  protected def commit(value: T): Unit

  /**
   * Rolls back to the supplied value for this Transactable. This will be called by the transaction and should never
   * be invoked manually.
   */
  protected def rollback(value: T) = {}
}

object Transactable {
  private[transaction] def commit[T](transactable: Transactable[T], value: T) = transactable.commit(value)

  private[transaction] def rollback[T](transactable: Transactable[T], value: T) = transactable.rollback(value)
}