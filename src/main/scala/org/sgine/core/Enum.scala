package org.sgine.core

class Enum {
	lazy val name: String = generateName()
	lazy val ordinal = generateOrdinal()
	lazy val enumeratedClass = generateEnumeratedClass()
	lazy val parentName = generateParentName()
	
	private def generateName() = {
		val s = getClass.getSimpleName
		s.substring(s.indexOf('$') + 1, s.length - 1)
	}
	
	private def generateEnumeratedClass() = {
		val enumClassName = getClass.getName
		val className = enumClassName.substring(0, enumClassName.indexOf('$') + 1)
		Class.forName(className)
	}
	
	private def generateParentName() = enumeratedClass.getSimpleName.substring(0, enumeratedClass.getSimpleName.length - 1) 
	
	private def generateOrdinal() = {
		val indexOf = enumeratedClass.getMethod("indexOf", classOf[String])
		val instance = enumeratedClass.getField("MODULE$").get()
		val result = indexOf.invoke(instance, name)
		
		result.asInstanceOf[Int]
	}
	
	override def toString() = parentName + "." + name
}