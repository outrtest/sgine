package org.sgine.opengl.generator

import java.net.URL
import xml.XML
import io.Source

/**
 * 
 *
 * Date: 2/13/11
 * @author Matt Hicks <mhicks@sgine.org>
 */
object GLNameGenerator {
  val url = "http://www.opengl.org/sdk/docs/man/xhtml/index.html"

  def generate() = {
    val source = Source.fromURL(url)
    val html = source.mkString.replaceAll("[&]nbsp", "")
    val xml = XML.loadString(html)
    (xml \\ "a").collect {
      case result if ((result \ "@target").text == "pagedisp") => result.text
    }.toList
  }
}