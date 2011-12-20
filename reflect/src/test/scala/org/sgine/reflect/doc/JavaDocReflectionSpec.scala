package org.sgine.reflect.doc

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class JavaDocReflectionSpec extends WordSpec with ShouldMatchers {
  var jdf: JavaDocReflection = _
  val clazz = classOf[String]
  val charAt = clazz.getMethod("charAt", classOf[Int])
  val codePointCount = clazz.getMethod("codePointCount", classOf[Int], classOf[Int])
  val intern = clazz.getMethod("intern")

  "JavaDocReflection" when {
    "instantiated" should {
      "not throw an exception" in {
        jdf = new JavaDocReflection(clazz.getName)
      }
      "not be null" in {
        jdf should not be (null)
      }
    }
    "invoked on String.charAt" should {
      var md: MethodDocumentation = null
      "not return null" in {
        md = jdf.method(charAt)
        md should not be (null)
      }
      "have one arg" in {
        md.args.length should be(1)
      }
      "have 'index' as the first argument" in {
        md.args(0).name should be("index")
      }
      "have Int as the first argument type" in {
        md.args(0).`type` should be(classOf[Int])
      }
      "have Char as the return type" in {
        md.returnClass.`type` should be(classOf[Char])
      }
      "link to the proper URL" in {
        md.url should be("http://download.oracle.com/javase/6/docs/api/java/lang/String.html#charAt(int)")
      }
    }
    "invoked on String.codePointCount" should {
      var md: MethodDocumentation = null
      "not return null" in {
        md = jdf.method(codePointCount)
        md should not be (null)
      }
      "have two args" in {
        md.args.length should be(2)
      }
      "have 'beginIndex' as the first argument" in {
        md.args(0).name should be("beginIndex")
      }
      "have Int as the first argument type" in {
        md.args(0).`type` should be(classOf[Int])
      }
      "have the proper documentation on the first argument" in {
        md.args(0).doc.get.text should be("the index to the first char of the text range.")
      }
      "have 'endIndex' as the second argument" in {
        md.args(1).name should be("endIndex")
      }
      "have Int as the second argument type" in {
        md.args(1).`type` should be(classOf[Int])
      }
      "have the proper documentation on the second argument" in {
        md.args(1).doc.get.text should be("the index after the last char of the text range.")
      }
      "have Int as the return type" in {
        md.returnClass.`type` should be(classOf[Int])
      }
      "have the proper documentation on the return type" in {
        md.returnClass.doc.get.text should be("the number of Unicode code points in the specified text range")
      }
      "link to the proper URL" in {
        md.url should be("http://download.oracle.com/javase/6/docs/api/java/lang/String.html#codePointCount(int, int)")
      }
    }
    "invoked on String.intern" should {
      var md: MethodDocumentation = null
      "not return null" in {
        md = jdf.method(intern)
        md should not be (null)
      }
      "have no args" in {
        md.args.length should be(0)
      }
      "have String as the return type" in {
        md.returnClass.`type` should be(classOf[String])
      }
      "link to the proper URL" in {
        md.url should be("http://download.oracle.com/javase/6/docs/api/java/lang/String.html#intern()")
      }
    }
  }
}