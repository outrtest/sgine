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

import collection.mutable.ListBuffer
import event.{ChildRemovedEvent, ChildAddedEvent, ContainerEventSupport}

/**
 * MutableContainer as the name suggests is a mutable implementation of Container.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class MutableContainer[T] extends Container[T] with ContainerEventSupport {
  private val buffer = new ListBuffer[T]

  /**
   * Represents the children of this container.
   */
  object contents extends Seq[T] {
    def iterator = buffer.iterator

    /**
     * Lookup a child by index.
     */
    def apply(index: Int) = buffer(index)

    /**
     * The number of children in this container.
     */
    def length = buffer.length

    /**
     * Inserts a child into this container and assigns this container as the parent if the child is
     * of type Element.
     */
    def +=(child: T) = synchronized {
      buffer += child

      child match {
        case element: Element => Element.assignParent(element, MutableContainer.this)
        case _ =>
      }

      if (containerChange.shouldFire) containerChange.fire(new ChildAddedEvent(MutableContainer.this, child))
    }

    /**
     * Removes the supplied child from this container and nullifies parent if the child is of type
     * Element.
     */
    def -=(child: T) = synchronized {
      buffer -= child

      child match {
        case element: Element => Element.assignParent(element, null)
        case _ =>
      }

      if (containerChange.shouldFire) containerChange.fire(new ChildRemovedEvent(MutableContainer.this, child))
    }
  }
}