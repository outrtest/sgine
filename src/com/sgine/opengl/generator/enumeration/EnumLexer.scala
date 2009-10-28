package com.sgine.opengl.generator.enumeration

import util.parsing.combinator.lexical.StdLexical
import util.parsing.combinator.ImplicitConversions
import util.matching.Regex

/**
 * Divides up the enumeration data input file into tokens that can be parsed.
 */
class EnumLexer extends StdLexical with ImplicitConversions {
  
  delimiters ++= Set("(", ")", ",", ".", ":", ";", "=")

  reserved ++= Set("define", "enum", "use")

  // Overriding the default whitespace parser
  override def whitespace: Parser[Any] = regex(whiteSpaceRegexp)

  // Skip whitespace and comments starting with #
  val whiteSpaceRegexp = """((\s+)|(?:#.*))+""".r


  /**
   * A parser that matches a regex string
   */
  private def regex(r: Regex): Parser[String] = new Parser[String] {
    def apply(in: Input) = {
      val source = in.source
      val offset = in.offset
      val start = offset 
      (r findPrefixMatchOf (source.subSequence(start, source.length))) match {
        case Some(matched) =>
          Success(source.subSequence(start, start + matched.end).toString,
            in.drop(start + matched.end - offset))
        case None =>
          Success("", in)
      }
    }
  }

}
