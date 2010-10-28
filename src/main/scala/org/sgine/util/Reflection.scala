package org.sgine.util

object Reflection {
	private val boxing = List(
						classOf[Boolean] -> classOf[java.lang.Boolean],
						classOf[Byte] -> classOf[java.lang.Byte],
						classOf[Char] -> classOf[java.lang.Character],
						classOf[Short] -> classOf[java.lang.Short],
						classOf[Int] -> classOf[java.lang.Integer],
						classOf[Long] -> classOf[java.lang.Long],
						classOf[Float] -> classOf[java.lang.Float],
						classOf[Double] -> classOf[java.lang.Double]
					 )
	
	def boxed(c: Class[_]) = {
		if (c.isPrimitive) {
			boxing.find(t => t._1 == c).get._2
		} else {
			c
		}
	}
}