package com.sgine.opengl.generator.enumeration

import util.parsing.combinator.lexical.StdLexical
import util.parsing.combinator.ImplicitConversions
import util.matching.Regex
import scala.util.parsing.input.CharArrayReader.EofCh

/**
 * Divides up the enumeration data input file into tokens that can be parsed.
 */
class EnumLexer extends StdLexical with ImplicitConversions {

  /**Hexanumeric literal tokens */
  case class HexLit(s: String) extends NumberLiteral("0x" + s) {
    override def toString = "0x" + chars
  }

  /**Deciamalnumeric literal tokens */
  case class DecimalLit(s: String) extends NumberLiteral(s) {
    override def toString = chars
  }

  /**numerical literal tokens */
  case class NumberLiteral(chars: String) extends Token {
  }

  override def token: Parser[Token] =
    (identChar ~ rep(identChar | digit) ^^ {case first ~ rest => processIdent(first :: rest mkString "")}
            | '0' ~ (elem('x') | elem('X')) ~ rep(digit | letter) ^^ {case zero ~ x ~ rest => HexLit(rest mkString "")}
            | digit ~ rep(digit) ^^ {case first ~ rest => DecimalLit(first :: rest mkString "")}
            | EofCh ^^^ EOF
            | delim
            | failure("illegal character")
            )

  delimiters ++= Set("(", ")", ",", ".", ":", ";", "=")

  reserved ++= Set("define", "enum", "use")

  // Overriding the default whitespace parser
  override def whitespace: Parser[Any] = regex(whiteSpaceRegexp)

  // Skip whitespace and comments starting with #
  val whiteSpaceRegexp = """((\s+)|(?:#.*))+""".r


  /**
   * A parser that matches a regexp string
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


