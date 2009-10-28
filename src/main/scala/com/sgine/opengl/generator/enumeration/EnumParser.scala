package com.sgine.opengl.generator.enumeration

import util.parsing.combinator.syntactical.StdTokenParsers

/**
 * Parses the enumeration specification file http://www.opengl.org/registry/api/enum.spec into Enum instances.
 */
class EnumParser extends StdTokenParsers {

  // Setup lexer
  type Tokens = EnumLexer
  val lexical = new EnumLexer

  // Parser
  def enums : Parser[List[Enum]] = rep( enum )
  def enum : Parser[Enum] = "enum" ^^ { x => null }

}
