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
   * All constructors on this class.
   */
  lazy val constructors: List[EnhancedConstructor] = javaClass.getConstructors.toList.map(c => new EnhancedConstructor(this, c))

  /**
   * All methods on this class.
   */
  lazy val methods: List[EnhancedMethod] = {
    val javaMethods = javaClass.getMethods.toSet ++ javaClass.getDeclaredMethods.toSet
    javaMethods.toList.map(m => new EnhancedMethod(this, m))
  }

  /**
   * Finds a method by the absoluteSignature.
   */
  def methodBySignature(signature: String) = methods.find(m => m.absoluteSignature == signature || m.signature == signature)

  /**
   * Finds a method from the supplied name and args.
   */
  def method(name: String, args: EnhancedClass*): Option[EnhancedMethod] = methods.find(m => m.name == name && m.argsMatch(args))

  /**
   * Finds a method from the supplied name and argument names and values.
   */
  def methodByArgs(name: String, args: List[(String, Any)]) = method(name, args.map(t => t._1 -> EnhancedClass.fromValue(t._2)))

  /**
   * Finds a method from the supplied name and argument names and types.
   */
  def method(name: String, args: List[(String, EnhancedClass)]) = methods.find(m => m.hasArgs(args))

  /**
   * Finds the first method match for the name supplied.
   */
  def methodByName(name: String) = methods.find(m => m.name == name)

  /**
   * The instance of this class if it exists. This is particularly useful on companion classes to get the singleton
   * instance they are stored within.
   */
  lazy val instance = javaClass.getFields.find(f => f.getName == "MODULE$").map(f => f.get(null))

  /**
   * True if this is a companion object.
   */
  def isCompanion = instance != None

  /**
   * The companion class to this class if it exists.
   */
  lazy val companion: Option[EnhancedClass] = try {
    Some(Class.forName(javaClass.getName + "$"))
  } catch {
    case exc: ClassNotFoundException => None
  }
  // TODO: add support for class-level docs

  lazy val caseValues: List[CaseValue] = {
    val fieldNames = javaClass.getDeclaredFields.map(f => f.getName).toList
    fieldNames.map(name => CaseValue(name, this))
  }

  lazy val copyMethod = methodByName("copy")

  def caseValue(name: String) = caseValues.find(cv => cv.name == name)

  def copy[T](instance: AnyRef, args: Map[String, Any]) = {
    val cm = copyMethod.getOrElse(throw new NullPointerException("No copy method for this class"))
    cm[T](instance, args)
  }

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
  // Set up defaults
  val Integer = EnhancedClass(classOf[Int])

  register(classOf[java.lang.Integer], Integer)

  def apply(clazz: Class[_]) = class2EnhancedClass(clazz)

  def fromValue(value: Any) = value match {
    case null => null
    case ref: AnyRef => apply(ref.getClass)
  }

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