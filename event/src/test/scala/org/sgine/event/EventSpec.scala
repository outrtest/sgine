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

package org.sgine.event

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import java.util.concurrent.atomic.AtomicInteger
import org.sgine.concurrent.{Executor, Time}
import org.sgine.{Priority, ProcessingMode}

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 6/21/11
 */
class EventSpec extends FlatSpec with ShouldMatchers {
  val test = new Test()
  val count = new AtomicInteger(0)

  "StringEventSupport" should "have no listeners" in {
    test.strings.hasListeners should equal(false)
  }

  it should "add a listener" in {
    val listener = (s: String) => count.addAndGet(1)
    test.strings.hasListeners should equal(false)
    test.strings += listener
    test.strings.hasListeners should equal(true)
  }

  it should "receive synchronous events" in {
    count.get should equal(0)
    test.strings.fire("Test 1")
    count.get should equal(1)
  }

  it should "clear listeners" in {
    test.strings.clear()
    test.strings.hasListeners should equal(false)
  }

  it should "receive asynchronous events" in {
    count.set(0)
    test.strings.asynchronous {
      case s => count.addAndGet(1)
    }
    test.strings.fire("Test 1")
    Time.waitFor(1.0) {
      count.get == 1
    } should equal(true)
  }

  it should "receive concurrent events" in {
    count.set(0)
    test.strings.clear()
    test.strings.concurrent {
      case s => count.addAndGet(1)
    }
    test.strings.fire("Test 1")
    Time.waitFor(1.0) {
      count.get == 1
    } should equal(true)
  }

  it should "wait for a specific event" in {
    test.strings.clear()
    Executor.invoke {
      Thread.sleep(100)
      test.strings.fire("Test 1")
    }
    test.strings.waitFor(1.0) should equal(Some("Test 1"))
  }

  it should "fail to wait for a specific event" in {
    test.strings.clear()
    test.strings.waitFor(0.5) should equal(None)
  }

  it should "properly sort and stop propagation" in {
    test.strings.clear()
    count.set(0)
    val listener1 = (s: String) => count.addAndGet(1)
    val listener2 = (s: String) => {
      count.addAndGet(1)
      Event.stopPropagation()
    }
    val h1 = new EventHandler(listener1, ProcessingMode.Synchronous, Priority.Low)
    val h2 = new EventHandler(listener2, ProcessingMode.Synchronous, Priority.Normal)
    test.strings += h1
    test.strings += h2
    test.strings.fire("Hello")
    count.get should equal(1)
  }
}

class Test extends StringEventSupport