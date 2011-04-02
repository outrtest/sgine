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

package org.sgine.opengl.generator

import annotation.tailrec
import io.Source
import java.lang.reflect.{Field, Method}

/**
 * 
 *
 * Date: 2/8/11
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Combiner(methodNames: List[String], e1: ClassExtractor, e2: ClassExtractor) {
  val fields = combineFields(e1.fields, Nil)
  val methods = combineMethods(methodNames, Nil).reverse

  @tailrec
  private def combineFields(fields: List[Field], combined: List[CombinedField]): List[CombinedField] = {
    if (fields.length > 0) {
      val field1 = fields.head
      val list = e2.fields(field1.getName) match {
        case Some(field2) => {
          val value1 = field1.get(null)
          val value2 = field2.get(null)
          if (value1 != value2) throw new RuntimeException("Different values for field: " + field1.getName + " - " + value1 + " / " + value2)

          CombinedField(field1.getName, field1.getType, value1, field1, field2) :: combined
        }
        case None => combined
      }

      combineFields(fields.tail, list)
    } else {
      combined
    }
  }

  @tailrec
  private def combineMethods(methodNames: List[String], combined: List[CombinedMethods]): List[CombinedMethods] = {
    if (methodNames.length > 0) {
      val name = methodNames.head
      val leftMatches = e1.methods(name)
      val rightMatches = e2.methods(name)
      if (leftMatches.length == 0 || rightMatches.length == 0) {
        combineMethods(methodNames.tail, combined)
      } else {
        val cm = new CombinedMethods(name, leftMatches, rightMatches)
        combineMethods(methodNames.tail, cm :: combined)
      }
    } else {
      combined
    }
  }
}

