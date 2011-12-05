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

import collection.mutable.ArrayBuffer
import annotation.tailrec
import event.{ChildRemovedEvent, ChildAddedEvent, ContainerEvent}

/**
 * ContainerView represents a flat view of the hierarchical elements of a container. The view should represent the
 * current flat list of the referenced container at all times.
 *
 * The query function optionally defines a mechanism of excluding elements from the view.
 *
 * The sort function optionally defines the sort order for elements retrieved from this view.
 *
 * The filter function optionally defines a temporary filtering of the view. This differs from the query method as it is
 * re-validated against currently included and excluded items per change and on-demand via the refreshFilter() method.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ContainerView[T](val container: Container[_],
                       query: (T) => Boolean = null,
                       sort: (T, T) => Int = null,
                       filter: (T) => Boolean = null)(implicit manifest: Manifest[T]) extends Iterable[T] {
  private var queue = new ArrayBuffer[T] {
    def localArray = array.asInstanceOf[Array[AnyRef]]
  }
  private var excluded = new ArrayBuffer[T]

  private lazy val comparator = if (sort != null) {
    new java.util.Comparator[AnyRef] {
      def compare(o1: AnyRef, o2: AnyRef) = sort(o1.asInstanceOf[T], o2.asInstanceOf[T])
    }
  } else {
    null
  }

  refresh()

  // Add listeners
  container.listeners.child[ContainerEvent] {
    case added: ChildAddedEvent => {
      validateChild(added.child.asInstanceOf[AnyRef])
      refreshFilter()
      refreshSort()
    }
    case removed: ChildRemovedEvent => {
      invalidateChild(removed.child.asInstanceOf[AnyRef])
      refreshFilter()
      refreshSort()
    }
  }

  /**
   * Refreshes the entries of this ContainerView.
   */
  def refresh() = {
    // TODO: validate thread-safety
    queue.clear()
    excluded.clear()
    validateRecursive(container.contents)

    refreshFilter()
    refreshSort()
  }

  /**
   * Refreshes the filtering of the ContainerView.
   */
  def refreshFilter() = {
    // TODO: validate thread-safety
    if (filter != null) {
      filterRecursive(excluded, false) match {
        case Nil =>
        case add => {
          queue ++= add
          excluded --= add
        }
      }
      filterRecursive(queue, true) match {
        case Nil =>
        case remove => {
          queue --= remove
          excluded ++= remove
        }
      }
    }
  }

  private def filterRecursive(items: Seq[T], includes: Boolean, results: List[T] = Nil): List[T] = {
    if (!items.isEmpty) {
      val item = items.head
      val exclude = !filter(item)
      val updated = if (includes && exclude) {
        item :: results
      } else if (!includes && !exclude) {
        item :: results
      } else {
        results
      }
      filterRecursive(items.tail, includes, updated)
    } else {
      results
    }
  }

  /**
   * Refreshes the sorting of this view.
   */
  def refreshSort() = {
    // TODO: validate thread-safety
    if (comparator != null && !queue.isEmpty) java.util.Arrays.sort(queue.localArray, 0, queue.length, comparator)
  }

  @tailrec
  private def validateRecursive(children: Iterable[_]): Unit = {
    if (!children.isEmpty) {
      val child = children.head
      validateChild(child.asInstanceOf[AnyRef])

      validateRecursive(children.tail)
    }
  }

  // Validates if a child should be added and adds it
  // Additionally recurses
  private def validateChild(child: AnyRef) = {
    if (isValid(child.asInstanceOf[AnyRef])) {
      queue += child.asInstanceOf[T]
    }

    child match {
      case container: Container[_] => validateRecursive(container.contents)
      case _ =>
    }
  }

  @tailrec
  private def invalidateRecursive(children: Iterable[_]): Unit = {
    if (!children.isEmpty) {
      val child = children.head
      invalidateChild(child.asInstanceOf[AnyRef])

      invalidateRecursive(children.tail)
    }
  }

  private def invalidateChild(child: AnyRef) = {
    queue -= child.asInstanceOf[T]

    child match {
      case container: Container[_] => invalidateRecursive(container.contents)
      case _ =>
    }
  }

  private def isValid(child: AnyRef) = {
    manifest.erasure.isAssignableFrom(child.getClass) && (query == null || query(child.asInstanceOf[T]))
  }

  def iterator = queue.iterator
}