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

import io.Source
import java.net.URL
import xml.{Node, Elem, XML}

class AndroidDocReflection(className: String) {
  val url = "http://developer.android.com/reference/" + className.replaceAll("[.]", "/") + ".html"
  val source = Source.fromURL(new URL(url), "UTF-8")
  val string = cleanup(source.mkString)
  val xml = XML.loadString(string)

  def methodArgs(name: String): List[String] = {
    val h4 = (xml \\ "h4").find(n => (n \ "span").length >= 3 && (n \ "span")(1).text == name)
    val args = h4.map(h4 => processArgs((h4 \ "span")(2).text)).get
    args.map(text => text.substring(text.indexOf(' ') + 1)).map(updateArg).toList
  }

  private def processArgs(s: String) = {
    val argString = s.substring(1, s.length - 1)
    argString.split(", ")
  }

  private val updateArg = (arg: String) => arg match {
    case "type" => "`type`"
    case s => s
  }

  private def cleanup(s: String) = {
    var t = s.replaceAll("[<]meta(.*)[>]", "<meta$1/>")
    t = t.replaceAll("&nbsp;", " ")
    t = t.replaceAll("cellspacing=0", "cellspacing=\"0\"")
    t
  }
}