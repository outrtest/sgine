package com.sgine.opengl.generator.enumeration

/**
 * Represents an enumeration in OpenGL, as parsed from http://www.opengl.org/registry/api/enum.spec
 */
case class Enum(name : String, values : List[Const] )
