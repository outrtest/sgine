package org.sgine

trait Enum {
  lazy val name = generateName()
  lazy val parent = generateParent()
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

  private def generateParentName() = {
    val s = enumeratedClass.getSimpleName
    s.substring(0, s.length - 1)
  }

  private def generateParent() = enumeratedClass.getField("MODULE$").get().asInstanceOf[Enumerated[_]]

  private def generateOrdinal() = {
    val indexOfMethod = enumeratedClass.getMethod("indexOf", classOf[String])
    val result = indexOfMethod.invoke(parent, name)
    result.asInstanceOf[Int]
  }

  override def toString() = parentName + "." + name
}