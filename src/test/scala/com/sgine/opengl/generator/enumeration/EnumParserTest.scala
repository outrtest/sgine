package com.sgine.opengl.generator.enumeration

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Assertions._


/**
 * Specification for EnumParser.
 *
 */
class EnumParserTest extends FlatSpec with ShouldMatchers {

  
  "EnumParser" should "produce an empty list for empty input" in {

    checkParse("", Nil)
  }

  def checkParse(input: String, expected: List[Enum]) {
    val parser = new EnumParser()
    parser.enums(new parser.lexical.Scanner(input)) match {
      case parser.Success(enums, _) => expected === enums
      println(enums)
      case f: parser.Failure => fail(f.toString)
      case e: parser.Error => fail(e.toString)
    }
  }

}
