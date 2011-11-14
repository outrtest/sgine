package org.sgine.reflect

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class EnhancedSpec extends WordSpec with ShouldMatchers {
  var ec: EnhancedClass = _
  var method: EnhancedMethod = _

  val tc = new TestClass

  "EnhancedClass" when {
    "requested on String" should {
      "not return null" in {
        ec = classOf[String]
        ec should not be (null)
      }
      "have the proper name" in {
        ec.name should be("String")
      }
      "have the proper number of methods" in {
        ec.methods.length should be(72)
      }
      "not have a companion class" in {
        ec.companion should be(None)
      }
    }
    "request on TestClass" should {
      "not return null" in {
        ec = classOf[TestClass]
        ec should not be (null)
      }
      "have the proper name" in {
        ec.name should be("org.sgine.reflect.TestClass")
      }
      "have the proper number of methods" in {
        ec.methods.length should be(12)
      }
      "have a companion class" in {
        ec.companion should not be (None)
      }
      "have the testMethod" in {
        method = ec.method("testMethod", classOf[String], classOf[Int]).getOrElse(null)
        method should not be (null)
      }
      "invoke testMethod" in {
        method[String](tc, "Test", 2) should be("Test 2")
      }
      "have two arguments: s and i" in {
        method.args.length should be(2)
        method.args(0).name should be("s")
        method.args(1).name should be("i")
      }
      "have one default argument" in {
        method.args(0).hasDefault should be(false)
        method.args(1).hasDefault should be(true)
      }
      "have 5 for the default argument" in {
        method.args(1).default(tc) should be(Some(5))
      }
      "have String for the returnType" in {
        method.returnType.`type` should be(classOf[String])
      }
      "have a well defined toString on EnhancedMethod" in {
        method.toString should be("org.sgine.reflect.TestClass.testMethod(s: String, i: Int): String")
      }
    }
  }
}

class TestClass {
  def testMethod(s: String, i: Int = 5) = s + " " + i
}

object TestClass {
  def testCompanion() = "companion class: " + getClass.getName
}