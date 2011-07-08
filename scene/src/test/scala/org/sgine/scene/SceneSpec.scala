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

import event.{ChildRemovedEvent, ChildAddedEvent}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class SceneSpec extends WordSpec with ShouldMatchers {
  "ImmutableContainer" when {
    "created" should {
      val container = new ImmutableContainer(List("One", "Two", "Three"))
      "have three elements" in {
        container.children.length should be(3)
      }
    }
  }

  "MutableContainer" when {
    "created" should {
      val container = new MutableContainer[String]()
      var added: ChildAddedEvent = null
      var removed: ChildRemovedEvent = null
      container.containerChange.synchronous {
        case event: ChildAddedEvent => added = event
        case event: ChildRemovedEvent => removed = event
      }
      "be empty" in {
        container.children.length should be(0)
      }
      "add one element" in {
        container += "One"
      }
      "have one element" in {
        container.children.length should be(1)
      }
      "have the correct element" in {
        container.children.head should be("One")
      }
      "throw a ChildAddedEvent" in {
        added should not be (null)
      }
      "reference the correct parent in the event" in {
        added.parent should be(container)
      }
      "reference the corrent child in the event" in {
        added.child should be("One")
      }
      "add another element" in {
        added = null
        container += "Two"
      }
      "have two elements" in {
        container.children.length should be(2)
      }
      "have the correct elements" in {
        container.children.head should be("One")
        container.children.tail.head should be("Two")
      }
      "throw another ChildAddedEvent" in {
        added should not be (null)
      }
      "reference the correct parent in the second add event" in {
        added.parent should be(container)
      }
      "reference the corrent child in the second add event" in {
        added.child should be("Two")
      }
      "remove an element" in {
        added = null
        container -= "One"
      }
      "have one element again" in {
        container.children.length should be(1)
      }
      "have the correct element again" in {
        container.children.head should be("Two")
      }
      "throw a ChildRemovedEvent" in {
        removed should not be (null)
      }
      "reference the correct parent in the remove event" in {
        removed.parent should be(container)
      }
      "reference the correct child in the remove event" in {
        removed.child should be("One")
      }
    }
  }
}