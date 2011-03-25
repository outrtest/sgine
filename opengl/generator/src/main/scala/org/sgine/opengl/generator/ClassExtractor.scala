package org.sgine.opengl.generator

import annotation.tailrec
import java.lang.reflect.{Field, Method}
import java.lang.reflect.Modifier._

/**
 * 
 *
 * Date: 2/8/11
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ClassExtractor(static: Boolean) {
  private var _fields: List[Field] = Nil
  private var _methods: List[Method] = Nil

  var classes: List[Class[_]] = Nil
  def fields = _fields
  def methods = _methods

  def process() = {
    recurseClasses(classes)
  }

  def fields(name: String) = _fields.find(_.getName == name)

  def methods(name: String) = _methods.filter {
    method => {
      method.getName.startsWith(name) && (method.getName.length - name.length) <= 2
    }
  }

  def methodNames() = _methods.foldLeft[List[String]](Nil)((list, method) => if (list.contains(method.getName)) list else method.getName :: list)

  @tailrec
  private def recurseClasses(classes: List[Class[_]]): Unit = {
    if (classes.length > 0) {
      val c = classes.head

      // Populate fields and methods
      populateFields(c.getDeclaredFields.toList)
      populateMethods(c.getDeclaredMethods.toList)

      // Reverse the list
      _fields = _fields.sortWith((a, b) => a.getName.compareTo(b.getName) > 0)
      _methods = _methods.sortWith((a, b) => a.getName.compareTo(b.getName) > 0)

      recurseClasses(classes.tail)
    }
  }

  @tailrec
  private def populateFields(fields: List[Field]): Unit = {
    if (fields.length > 0) {
      val f = fields.head
      if (isStatic(f.getModifiers) && isPublic(f.getModifiers)) {
        _fields = f :: _fields
      }

      populateFields(fields.tail)
    }
  }

  @tailrec
  private def populateMethods(methods: List[Method]): Unit = {
    if (methods.length > 0) {
      val m = methods.head
      if (isStatic(m.getModifiers) == static && isPublic(m.getModifiers)) {
        _methods = m :: _methods
      }

      populateMethods(methods.tail)
    }
  }
}