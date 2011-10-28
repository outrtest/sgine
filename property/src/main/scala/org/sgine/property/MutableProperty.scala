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

package org.sgine.property

import event.{ValidationFailEvent, ValidationFailEventSupport}
import java.util.concurrent.atomic.AtomicReference

import org.sgine.transaction.Transactable
import org.sgine.event.{Listenable, Bindable, ChangeEvent}
import org.sgine.scene.Element

/**
 * MutableProperty represents a property that is mutable. Additionally, many features are supported herein:
 *  - Binding via the Bindable trait
 *  - Fires ChangeEvents
 *  - Allows for delegation of getter and setter to functions
 *  - Supports a default function to define a value until an initial value is set
 *  - Allows filtering incoming values upon change
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class MutableProperty[T](p: Listenable = null)(implicit val manifest: Manifest[T])
    extends Property[T]
    with ((T) => Any)
    with ValidationFailEventSupport
    with Bindable[T]
    with Transactable[T] {
  Element.assignParent(this, p)

  lazy private val ref = new AtomicReference[T]

  private def v = ref.get

  private def v_=(v: T) = ref.set(v)

  private var getter: () => T = _
  private var setter: (T) => Any = _

  /**
   * Filters the incoming value upon change if defined.
   *
   * Defaults to null.
   */
  var filter: (T) => T = _

  /**
   * Validates incoming value upon change. If Some is returned the value is not applied.
   * The function may also choose to throw an exception depending on how validation handling
   * is implemented. If Some is returned the property will throw a ValidationFailEvent with
   * the validation failure String and the new value will not be assigned. All validations are
   * executed which may cause multiple validation fail events to be thrown for a single assignment.
   *
   * Defaults to Nil.
   */
  var validations: List[(T) => Option[String]] = Nil

  /**
   * Defines whether the defaultFunction should be used as the backing value. This defaults to true
   * until a value is explicitly set, but unless defaultFunction is specified it doesn't do anything.
   */
  var default = true
  /**
   * The function to use to determine the default value.
   */
  var defaultFunction: () => T = _

  def this(initial: T)(implicit manifest: Manifest[T]) = {
    this () (manifest)
    apply(initial)
  }

  def value_=(v: T) = apply(v)

  def :=(v: T) = apply(v)

  def apply() = {
    if (default && defaultFunction != null) {
      defaultFunction()
    }
    else if (getter != null) {
      getter()
    }
    else if (isTransaction) {
      transactionValue
    }
    else {
      v
    }
  }

  def apply(v: T) = {
    if (isValid(v)) {
      val value = if (filter != null) {
        filter(v)
      }
      else {
        v
      }
      val oldValue = if (setter != null) {
        val oldValue = getter()
        setter(value)
        oldValue
      }
      else if (isTransaction) {
        transactionValue = value
        this.v
      }
      else {
        val oldValue = this.v
        this.v = value
        oldValue
      }
      default = false // TODO: this is problematic with transactions...
      if (!isTransaction) {
        if (change.shouldFire) {
          change.fire(ChangeEvent(name, oldValue, value))
        }
      }
    }
  }

  /**
   * Define delegate getter and setter methods to override the local value.
   */
  def delegate(getter: () => T = null, setter: (T) => Any = null) = {
    this.getter = getter
    this.setter = setter
  }

  private def isValid(v: T) = {
    if (!validations.isEmpty) {
      var failure = false
      def validate(validator: (T) => Option[String]) = {
        validator(v) match {
          case Some(message) => {
            // Fire validation fail event per validation fail
            if (validationFailure.shouldFire) {
              validationFailure.fire(ValidationFailEvent(v, message))
            }
            failure = true
          }
          case None =>
        }
      }
      validations.foreach(validate)
      !failure
    }
    else {
      true
    }
  }

  protected def commit(value: T) = apply(value)
}