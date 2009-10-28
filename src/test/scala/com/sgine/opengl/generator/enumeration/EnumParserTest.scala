package com.sgine.opengl.generator.enumeration

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers


/**
 * Specification for EnumParser.
 *
 */
class EnumParserTest extends FlatSpec with ShouldMatchers {

  
  "EnumParser" should "produce an empty list for empty input" in {

    assertParseEquals("", Nil)
  }

  "EnumParser" should "parse a simple enum sequence" in {

    val enumspec =
    """
    ###############################################################################

    Boolean enum:
      FALSE						= 0
      TRUE						= 1

    ###############################################################################
    
    AccumOp enum:
      ACCUM						= 0x0100
      LOAD						= 0x0101
      RETURN						= 0x0102
      MULT						= 0x0103
      ADD						= 0x0104

    ###############################################################################
    """

    assertParseEquals(enumspec, List(
      Enum( "Boolean", "", List(
        Const( "FALSE", "", "0" ),
        Const( "TRUE",  "", "1" )
        ) ),
      Enum( "AccumOp", "", List(
        Const( "ACCUM",   "", "0x0100" ),
        Const( "LOAD",    "", "0x0101" ),
        Const( "RETURN",  "", "0x0102" ),
        Const( "MULT",    "", "0x0103" ),
        Const( "ADD",     "", "0x0104" )
        ) )
      ) )
  }



  def assertParseEquals(input: String, expected: List[Enum]) {
    val parser = new EnumParser()
    parser.enums(new parser.lexical.Scanner(input)) match {
      case parser.Success(enums, _) => assert( expected === enums )
      case f: parser.Failure => fail(f.toString)
      case e: parser.Error => fail(e.toString)
    }
  }

}
