package org.sgine.opengl.generator

import annotation.tailrec
import io.Source
import java.lang.reflect.{Modifier, Method}
import com.thoughtworks.paranamer.BytecodeReadingParanamer

/**
 * 
 *
 * Date: 2/8/11
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class CombinedMethod(name: String, args: Array[Class[_]], argNames: List[String], returnType: Class[_], left: MethodSignature, right: MethodSignature)



