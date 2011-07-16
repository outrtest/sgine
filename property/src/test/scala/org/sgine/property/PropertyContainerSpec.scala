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

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class PropertyContainerSpec extends WordSpec with ShouldMatchers {
  "PropertyContainer" when {
    "created with two properties" should {
      val container = new PropertyContainer {
        val p1 = new ImmutableProperty[String]("Hello")
        val p2 = new MutableProperty[String]("Goodbye")
      }
      "have a length of two" in {
        container.properties.length should equal(2)
      }
      "have p1 as the first property" in {
        container.properties(0) should equal(container.p1)
      }
      "have p2 as the second property" in {
        container.properties(1) should equal(container.p2)
      }
      "have p1's parent as the container" in {
        container.p1.parent() should equal(container)
      }
      "have p2's parent as the container" in {
        container.p2.parent() should equal(container)
      }
      "have p1's name as \"p1\"" in {
        container.p1.name should equal("p1")
      }
      "have p2's name as \"p2\"" in {
        container.p2.name should equal("p2")
      }
      "properly lookup p1 by name" in {
        container.property("p1") should equal(Some(container.p1))
      }
      "properly lookup p2 by name" in {
        container.property("p2") should equal(Some(container.p2))
      }
      "fail to lookup p3 by name" in {
        container.property("p3") should equal(None)
      }
    }
  }
}