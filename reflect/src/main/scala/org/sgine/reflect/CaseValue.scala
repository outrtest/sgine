package org.sgine.reflect

import java.lang.reflect.Modifier

/**
 * CaseValue represents a value on a case class.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class CaseValue(name: String, valueType: EnhancedClass, clazz: EnhancedClass) {
  lazy val getter = clazz.method(name)
  lazy val setter = clazz.methods.find(m => m.name == "%s_$eq".format(name))

  def isMutable = setter != None

  def isTransient = Modifier.isTransient(clazz.javaClass.getDeclaredField(name).getModifiers)

  def apply[T](instance: AnyRef) = getter.get.invoke[T](instance)

  def update(instance: AnyRef, value: Any) = setter.get.invoke[Any](instance, value)
}