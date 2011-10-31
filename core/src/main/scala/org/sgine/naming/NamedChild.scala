package org.sgine.naming

import org.sgine.reflect.EnhancedMethod

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait NamedChild {
  protected val typeClass = findTypeClass()

  protected def parent: NamingParent

  protected lazy val companion = Class.forName(typeClass.getName + "$").getField("MODULE$").get(this)

  private lazy val method = parent.method(this)

  lazy val name = method.name
  lazy val ordinal = determineOrdinal()

  private def findTypeClass(clazz: Class[_] = getClass): Class[_] = {
    if (clazz.getName.indexOf("$anon$") != -1) {
      findTypeClass(clazz.getSuperclass)
    } else {
      clazz
    }
  }

  private def determineOrdinal(count: Int = 0, list: List[EnhancedMethod] = parent.fields): Int = {
    if (list.isEmpty) {
      -1
    } else {
      val method = list.head
      if (this.method == method) {
        count
      } else {
        val index = if (typeClass.isAssignableFrom(method.returnType.`type`.javaClass)) {
          count + 1
        } else {
          count
        }
        determineOrdinal(index, list.tail)
      }
    }
  }
}