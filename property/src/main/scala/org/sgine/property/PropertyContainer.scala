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

import org.sgine.event.Listenable
import org.sgine.scene.Element

import com.googlecode.reflective._
import annotation.tailrec

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait PropertyContainer extends Listenable with Element with DelayedInit {
  private var _properties: Seq[Property[_]] = Nil

  def properties: Seq[Property[_]] = _properties

  def property(name: String) = properties.find(p => p.name == name)

  def delayedInit(x: => Unit) = {
    x
    _properties = loadProperties(getClass.methods).reverse
  }

  @tailrec
  private final def loadProperties(methods: List[EnhancedMethod], properties: List[Property[_]] = Nil): List[Property[_]] = {
    if (methods.isEmpty) {
      properties
    } else {
      val method = methods.head
      val list = if (method.args.isEmpty && classOf[Property[_]].isAssignableFrom(method.returnType.`type`.javaClass)) {
        val property = method[Property[_]](this)
        property._name = method.name
        Element.assignParent(property, this)
        property :: properties
      } else {
        properties
      }
      loadProperties(methods.tail, list)
    }
  }
}