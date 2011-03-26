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