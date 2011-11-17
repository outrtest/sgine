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

package org.sgine.scene

import org.sgine.event.Listenable

/**
 * Element is the base class for scene. Though not all items in a scene must mix in Element, doing
 * so allows for parent / child relationships and other benefits.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Element extends Listenable {
  override val parent: () => Element = new ElementParent()

  /**
   * Processes up the ancestry tree through parents to find the first matching ancestor of the
   * generic type T and invokes the supplied function on it.
   */
  def ancestor[T](f: T => Unit)(implicit manifest: Manifest[T]): Unit = {
    val p = parent()
    if (p != null) {
      if (manifest.erasure.isAssignableFrom(p.getClass)) {
        f(p.asInstanceOf[T])
      } else {
        p.ancestor(f)(manifest)
      }
    }
  }

  /**
   * Processes up the ancestry tree through parents executing the supplied function on all parents
   * that match the supplied generic type.
   */
  def ancestors[T](f: T => Unit)(implicit manifest: Manifest[T]): Unit = {
    val p = parent()
    if (p != null) {
      if (manifest.erasure.isAssignableFrom(p.getClass)) {
        f(p.asInstanceOf[T])
      }
      p.ancestor(f)(manifest)
    }
  }

  /**
   * Invokes the supplied method if this class matches the supplied generic type.
   *
   * Returns true if the generic type matched.
   */
  def apply[T](f: T => Unit)(implicit manifest: Manifest[T]): Boolean = {
    if (manifest.erasure.isAssignableFrom(getClass)) {
      f(this.asInstanceOf[T])
      true
    }
    else {
      false
    }
  }
}

/**
 * ElementParent is the default implementation of parental lookup on an Element.
 */
class ElementParent extends Function0[Element] with Function1[Element, Unit] {
  private var parent: Element = null

  def apply() = parent

  def apply(parent: Element) = this.parent = parent
}

object Element {
  /**
   * Assigns the parent to an element.
   */
  def assignParent(element: Element, parent: Listenable) = element.parent
      .asInstanceOf[(Listenable) => Unit](parent)
}