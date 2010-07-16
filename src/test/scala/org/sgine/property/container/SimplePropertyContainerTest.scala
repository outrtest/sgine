package org.sgine.property.container

import org.sgine.property._

object SimplePropertyContainerTest extends PropertyContainer {
	val t1 = new AdvancedProperty[String](null, this)
	
	def main(args: Array[String]): Unit = {
		println("t1: " + t1.name)
	}
}