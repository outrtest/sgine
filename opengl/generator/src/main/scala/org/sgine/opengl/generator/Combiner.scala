package org.sgine.opengl.generator

import annotation.tailrec
import io.Source
import java.lang.reflect.{Field, Method}

/**
 * 
 *
 * Date: 2/8/11
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Combiner(methodNames: List[String], e1: ClassExtractor, e2: ClassExtractor) {
  val fields = combineFields(e1.fields, Nil)
  val methods = combineMethods(methodNames, Nil).reverse

  @tailrec
  private def combineFields(fields: List[Field], combined: List[CombinedField]): List[CombinedField] = {
    if (fields.length > 0) {
      val field1 = fields.head
      val list = e2.fields(field1.getName) match {
        case Some(field2) => {
          val value1 = field1.get(null)
          val value2 = field2.get(null)
          if (value1 != value2) throw new RuntimeException("Different values for field: " + field1.getName + " - " + value1 + " / " + value2)

          CombinedField(field1.getName, field1.getType, value1, field1, field2) :: combined
        }
        case None => combined
      }

      combineFields(fields.tail, list)
    } else {
      combined
    }
  }

  @tailrec
  private def combineMethods(methodNames: List[String], combined: List[CombinedMethods]): List[CombinedMethods] = {
    if (methodNames.length > 0) {
      val name = methodNames.head
      val leftMatches = e1.methods(name)
      val rightMatches = e2.methods(name)
      if (leftMatches.length == 0 || rightMatches.length == 0) {
        combineMethods(methodNames.tail, combined)
      } else {
        val cm = CombinedMethods(name, leftMatches, rightMatches)
        combineMethods(methodNames.tail, cm :: combined)
      }
    } else {
      combined
    }
  }
}

