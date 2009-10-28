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
  def enum  : Parser[Enum]       = ident ~ "enum" ~ ":" ~ rep( const ) ^^ { case name ~ keyword ~ colon ~ constants => Enum( name, constants ) }
  def const : Parser[Const]      = ident ~ "=" ~ numericLiteral ^^ { case name ~ eq ~ value => Const( name, value ) }



  /**
   * A parser which matches a numeric (hexa or decimal) literal
   */
  def numericLiteral: Parser[String] = elem("number", _.isInstanceOf[EnumLexer#NumberLiteral]) ^^ (_.chars)

}
