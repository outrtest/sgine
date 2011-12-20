package org.sgine.reflect

import doc.DocumentationReflection
import ref.SoftReference
import java.lang.reflect.Method

/**
 * Wraps a Class to provide more powerful functionality.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class EnhancedClass protected[reflect](val javaClass: Class[_]) {
  /**
   * The name of the class.
   */
  def name = EnhancedClass.convertClass(javaClass)

  /**
   * All methods on this class.
   */
  lazy val methods: List[EnhancedMethod] = javaClass.getMethods.toList.map(m => new EnhancedMethod(this, m))

  /**
   * Finds a method by the absoluteSignature.
   */
  def methodBySignature(signature: String) = methods.find(m => m.absoluteSignature == signature || m.signature == signature)

  /**
   * Finds a method from the supplied name and args.
   */
  def method(name: String, args: EnhancedClass*): Option[EnhancedMethod] = methods.find(m => m.name == name && m.argsMatch(args))

  /**
   * The companion class to this class if it exists.
   */
  lazy val companion: Option[EnhancedClass] = try {
    Some(Class.forName(javaClass.getName + "$"))
  } catch {
    case exc: ClassNotFoundException => None
  }
  // TODO: add support for class-level docs

  // Only called internally to avoid receiving methods not for this class
  private[reflect] def apply(m: Method): EnhancedMethod = {
    method(m.getName, m.getParameterTypes.map(c => class2EnhancedClass(c)): _*).get
  }

  protected[reflect] def getDocs = _docs.get match {
    case Some(df) => df
    case None => {
      _docs = generateDocRef()
      _docs()
    }
  }

  private var _docs = generateDocRef()

  private def generateDocRef() = new SoftReference[DocumentationReflection](DocumentationReflection(javaClass))

  override def toString = name

  override def equals(ref: Any) = ref match {
    case ec: EnhancedClass => javaClass == ec.javaClass
    case c: Class[_] => javaClass == c
    case _ => false
  }
}

object EnhancedClass {
  /**
   * Converts primitive classes to wrapper classes.
   */
  def convertClass(c: Class[_]) = c.getName match {
    case "boolean" => "Boolean"
    case "byte" => "Byte"
    case "int" => "Int"
    case "long" => "Long"
    case "float" => "Float"
    case "double" => "Double"
    case "void" => "Unit"
    case "java.lang.String" => "String"
    case "[I" => "Array[Int]"
    case s => s
  }
}