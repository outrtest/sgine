package org.sgine.util

import scala.xml._

object XMLUtil {
	val printer = new PrettyPrinter(255, 2)
	
	implicit def t2ua(t: Tuple2[String, String]) = new UnprefixedAttribute(t._1, t._2, Null)
	
	implicit def elem2eelem(e: Elem) = EnhancedElem(e)
	
	def save(elem: Elem, file: java.io.File) = {
		val s = printer.format(elem)
		val w = new java.io.FileWriter(file)
		try {
			w.write(s)
			w.flush()
		} finally {
			w.close()
		}
	}
}

case class EnhancedElem (e: Elem) {
	def +(child: Node) = Elem(e.prefix, e.label, e.attributes, e.scope, e.child ++ child: _*)
}