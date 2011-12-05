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
        container.contents.length should be(3)
      }
    }
  }

  "MutableContainer" when {
    val container = new MutableContainer[String]()
    var added: ChildAddedEvent = null
    var removed: ChildRemovedEvent = null
    container.listeners.synchronous {
      case event: ChildAddedEvent => added = event
      case event: ChildRemovedEvent => removed = event
    }
    "created" should {
      "be empty" in {
        container.contents.length should be(0)
      }
    }
    "adding \"One\"" should {
      "have one element" in {
        container.contents += "One"
        container.contents.length should be(1)
      }
      "have the correct element" in {
        container.contents.head should be("One")
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
    }
    "adding \"Two\"" should {
      "have two elements" in {
        added = null
        container.contents += "Two"
        container.contents.length should be(2)
      }
      "have the correct elements" in {
        container.contents.head should be("One")
        container.contents.tail.head should be("Two")
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
    }
    "removing \"One\"" should {
      "have one element again" in {
        added = null
        container.contents -= "One"
        container.contents.length should be(1)
      }
      "have the correct element again" in {
        container.contents.head should be("Two")
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

  "StaticContainer" when {
    class Test
    class Test2 extends Test
    class Test3 extends Test2
    val container = new StaticContainer[Test] {
      val one = new Test
      val two = new Test2
      val three = new Test3
    }
    "created as val with three elements" should {
      "have three elements" in {
        container.contents.length should be(3)
      }
    }
    object container2 extends StaticContainer[Test] {
      val one = new Test
      val two = new Test2
      val three = new Test3
    }
    "created as object with three elements" should {
      "have three elements" in {
        container2.contents.length should be(3)
      }
    }
  }

  "ContainerView" when {
    val container = new ImmutableContainer(List("One", "Two", "Three"))
    val container2 = new MutableContainer[AnyRef]()
    val container3 = new MutableContainer[String]()
    val container4 = new MutableContainer[String]()
    val container5 = new MutableContainer[String]()

    val containerView = new ContainerView[String](container)
    val containerView2 = new ContainerView[String](container2)
    val query = (s: String) => s.length > 3
    val containerView3 = new ContainerView[String](container3, query)
    val sort = (s1: String, s2: String) => s1.compare(s2)
    val containerView4 = new ContainerView[String](container4, sort = sort)
    var filterLength = 3
    val filter = (s: String) => s.length > filterLength
    val containerView5 = new ContainerView[String](container5, filter = filter)

    val ic = new ImmutableContainer[String](List("Uno"))

    "created on a container with three elements" should {
      "have three elements" in {
        containerView.size should be(3)
      }
    }
    "created on an empty container" should {
      "have no elements" in {
        containerView2.size should be(0)
      }
    }
    "adding \"One\" to the container" should {
      "have one element" in {
        container2.contents += "One"
        containerView2.size should be(1)
      }
    }
    "adding \"Two\" to the container" should {
      "have two elements" in {
        container2.contents += "Two"
        containerView2.size should be(2)
      }
    }
    "removing \"One\" from the container" should {
      "have one element" in {
        container2.contents -= "One"
        containerView2.size should be(1)
      }
      "have \"Two\" as the only item" in {
        containerView2.head should be("Two")
      }
    }
    "adding an ImmutableContainer(\"Uno\")" should {
      "have two elements" in {
        container2.contents += ic
        containerView2.size should be(2)
      }
      "define the parent container correctly" in {
        ic.parent() should be(container2)
      }
      "reference \"Two\" and \"Uno\" as the only elements" in {
        containerView2.head should be("Two")
        containerView2.tail.head should be("Uno")
      }
    }
    "removing an ImmutableContainer(\"Uno\")" should {
      "have one element" in {
        container2.contents -= ic
        containerView2.size should be(1)
      }
      "define the parent container as null" in {
        ic.parent() should be(null)
      }
      "reference \"Two\" as the only element" in {
        containerView2.head should be("Two")
      }
    }
    "adding items to a queried view" should {
      "have one element" in {
        container3.contents += "Three"
        containerView3.size should be(1)
      }
      "exclude an item via query" in {
        container3.contents += "Two"
        containerView3.size should be(1)
      }
    }
    "adding items to a sorted view" should {
      "have three elements" in {
        container4.contents += "One"
        container4.contents += "Two"
        container4.contents += "Three"
        containerView4.size should be(3)
      }
      "be sorted correctly" in {
        containerView4.head should be("One")
        containerView4.tail.head should be("Three")
        containerView4.tail.tail.head should be("Two")
      }
    }
    "adding items to a filtered view" should {
      "have one element visible after three inserts" in {
        container5.contents += "One"
        container5.contents += "Two"
        container5.contents += "Three"
        containerView5.size should be(1)
      }
      "have \"Three\" as the only visible element" in {
        containerView5.head should be("Three")
      }
      "have three elements after updating filter" in {
        filterLength = 0
        containerView5.refreshFilter()
        containerView5.size should be(3)
      }
      "have one element again after updating filter" in {
        filterLength = 3
        containerView5.refreshFilter()
        containerView5.size should be(1)
      }
      "have zero elements after updating filter" in {
        filterLength = 10
        containerView5.refreshFilter()
        containerView5.size should be(0)
      }
      "have three elements again after updating filter" in {
        filterLength = 0
        containerView5.refreshFilter()
        containerView5.size should be(3)
      }
    }
  }
}