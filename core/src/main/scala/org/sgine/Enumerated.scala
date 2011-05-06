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

package org.sgine

import scala.util.Random

/**
 * Enumerated must be mixed into the companion object for Enum implementation.
 */
trait Enumerated[E <: Enum] extends Traversable[E] {
  private lazy val r = new Random()

  /**
   * Sequence of all associated Enums
   */
  def values: Seq[E]

  /**
   * Number of Enums
   */
  override def size = values.size

  /**
   * Retrieve Enum by index
   */
  def apply(index: Int) = values(index)

  /**
   * Next Enum from current.
   *
   * @param wrap Wraps around back to the beginning if true. Defaults to true.
   */
  def next(current: E, wrap: Boolean = true) = current.ordinal match {
    case index if (index == size - 1) => head
    case index => apply(index)
  }

  /**
   * Previous Enum from current.
   *
   * @param wrap Wraps around back to the end if true. Defaults to true.
   */
  def previous(current: E, wrap: Boolean = true) = current.ordinal match {
    case 0 => last
    case index => apply(index)
  }

  /**
   * Retrieves a random Enum.
   */
  def random = values(r.nextInt(size))

  def foreach[U](f: E => U) = values.foreach(f)

  def valueOf(s: String) = values.find(e => e.name == s)

  def indexOf(e: E) = values.indexOf(e)

  def indexOf(s: String): Int = valueOf(s) match {
    case Some(e) => indexOf(e)
    case None => -1
  }
}