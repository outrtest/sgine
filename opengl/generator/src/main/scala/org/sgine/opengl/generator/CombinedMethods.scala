package org.sgine.opengl.generator

import java.lang.reflect.Method
import annotation.tailrec
import io.Source

/**
 * 
 *
 * Date: 2/8/11
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class CombinedMethods(name: String, left: List[Method], right: List[Method]) {
  private var _methods: List[CombinedMethod] = Nil
  def methods = _methods

  private var workLeft: List[Method] = left
  private var workRight: List[Method] = right

  generateMethods()

  private def generateMethods(): Unit = {
    findPerfect()

    if (methods.length == 0) {
      println(name + ": Unable to find match: " + left.length + " - " + right.length)
      println("\tLeft:")
      left.foreach(m => println("\t\t" + m))
      println("\tRight:")
      right.foreach(m => println("\t\t" + m))
      System.exit(0)
    } else {
      println("Matches found for: " + name + " - " + methods.length + ", Left over: " + workLeft.length + ", " + workRight.length)
    }
  }

  private def findPerfect() = {
    for (l <- workLeft; r <- workRight; if (l.getName == r.getName); if (parametersMatch(l, r))) {
      _methods = CombinedMethod(name, l.getParameterTypes, l.getReturnType, l, r) :: _methods
      workLeft = workLeft.filterNot(_ == l)
      workRight = workRight.filterNot(_ == r)
    }
  }

  private def parametersMatch(left: Method, right: Method) = {
    if (left.getParameterTypes.length == right.getParameterTypes.length) {
      left.getParameterTypes.zip(right.getParameterTypes).find(t => t._1 != t._2) match {
        case Some(b) => false
        case None => true
      }
    } else {
      false
    }
  }
}

case class CombinedMethod(name: String, args: Array[Class[_]], returnType: Class[_], left: Method, right: Method)