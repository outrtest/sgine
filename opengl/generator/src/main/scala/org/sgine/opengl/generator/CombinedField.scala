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
case class CombinedField(name: String, fieldType: Class[_], value: Any, leftOrigin: Field, rightOrigin: Field)

