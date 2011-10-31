package org.sgine.reflect.doc

import java.net.URL
import io.Source
import java.lang.reflect.Method
import annotation.tailrec

import org.sgine.reflect._

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class JavaDocReflection(className: String) extends DocumentationReflection {
  private lazy val url = JavaDocReflection.baseURL + className.replaceAll("[.]", "/") + ".html"
  private lazy val source = Source.fromURL(new URL(url), "UTF-8")
  private lazy val string = source.mkString

  def method(m: Method) = {
    val nameLookup = generateNameLookup(m)
    val offset = string.indexOf("<A NAME=\"" + nameLookup + "\">")
    val doc = between(string, offset, "<DD>", "<DD>").map(s => Documentation(cleanWhite(s)))
    val args = if (m.getParameterTypes.length == 0) {
      Nil
    } else {
      between(string, offset, "<DT><B>Parameters:</B>", "<DT><B>") match {
        case Some(s) => splitArgs(m.getParameterTypes.toList, s).reverse
        case None => Nil
      }
    }
    val retDocs = (between(string, offset, "<DT><B>Returns:</B><DD>", "<DT>") match {
      case Some(docs) => Some(docs)
      case None => between(string, offset, "<DT><B>Returns:</B><DD>", "</DL>")
    }).map(s => Documentation(cleanWhite(s)))
    val ret = DocumentedClass(null, m.getReturnType, retDocs)

    val link = url + "#" + nameLookup
    MethodDocumentation(args, ret, link, doc)
  }

  @tailrec
  private def splitArgs(paramTypes: List[Class[_]], s: String, args: List[DocumentedClass] = Nil): List[DocumentedClass] = {
    val break = s.indexOf("<DD>", 5) match {
      case -1 => s.length
      case i => i
    }
    val content = s.substring(4, break)
    val name = content.substring(6, content.indexOf("</CODE>"))
    val doc = content.substring(content.indexOf("</CODE>") + 10).trim match {
      case "" => None
      case s => Some(cleanWhite(s))
    }
    val dc = DocumentedClass(name, paramTypes.head, doc.map(s => Documentation(s)))
    val updated = dc :: args
    if (s.length == break) {
      updated
    } else {
      splitArgs(paramTypes.tail, s.substring(break), updated)
    }
  }
}

object JavaDocReflection extends DocMapper {
  var baseURL = "http://download.oracle.com/javase/6/docs/api/"

  def typeConversion(c: Class[_]) = c.getName match {
    case "[I" => "int[]"
    case "[L" => "long[]"
    case "[F" => "float[]"
    case "[D" => "double[]"
    case s => s
  }

  def apply(c: Class[_]) = new JavaDocReflection(c.getName)
}