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

import scala.xml._

object XMLUtil {
	val printer = new PrettyPrinter(255, 2)
	
	implicit def t2ua(t: Tuple2[String, String]) = new UnprefixedAttribute(t._1, t._2, Null)
	
	implicit def node2eelem(n: Node) = EnhancedElem(n.asInstanceOf[Elem])
	
	def save(elem: Elem, file: java.io.File) = {
		val s = printer.format(elem)
		val w = new java.io.FileWriter(file)
		try {
			w.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n")
			w.write(s)
			w.flush()
		} finally {
			w.close()
		}
	}
}

case class EnhancedElem (e: Elem) {
	def +(child: Node) = Elem(e.prefix, e.label, e.attributes, e.scope, e.child ++ child: _*)
	
	def \@(name: String) = (e \ ("@" + name)).text
	
	def \!(name: String) = (e \ name).head
}