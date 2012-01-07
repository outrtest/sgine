package org.sgine.reflect

/**
 * CaseValue represents a value on a case class.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class CaseValue(name: String, clazz: EnhancedClass) {
  lazy val getter = clazz.method(name)
  lazy val setter = clazz.methods.find(m => m.name == "%s_$eq".format(name))

  def isMutable = setter != None

  def apply[T](instance: AnyRef) = getter.get.invoke[T](instance)

  def update(instance: AnyRef, value: Any) = setter.get.invoke[Any](instance, value)
}