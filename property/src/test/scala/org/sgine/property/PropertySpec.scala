package org.sgine.property

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.sgine.event.{Listenable, ChangeEvent}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class PropertySpec extends WordSpec with ShouldMatchers {
  "Property values" when {
    "created without a default value" should {
      val sp = Property[String]()(null)
      val ip = Property[Int]()(null)
      "have the correct default values" in {
        sp() should equal(null)
        ip() should equal(0)
      }
    }
  }
  "Property listeners" when {
    var direct = 0
    var inner = 0
    var outter = 0
    PropertyTester.inner.p.listeners.synchronous {
      case event: ChangeEvent[_] => direct += 1
    }
    PropertyTester.inner.listeners.filter.descendant().synchronous {
      case event: ChangeEvent[_] => inner += 1
    }
    PropertyTester.listeners.filter.descendant().synchronous {
      case event: ChangeEvent[_] => outter += 1
    }
    "hierarchy is validate" should {
      "have the correct parent for the property" in {
        PropertyTester.inner.p.parent should equal(PropertyTester.inner)
      }
      "have the correct parent for inner" in {
        PropertyTester.inner.parent should equal(PropertyTester)
      }
      "have no parent for outter" in {
        PropertyTester.parent should equal(null)
      }
    }
    "changed" should {
      "update the value of the property" in {
        PropertyTester.inner.p := 5
      }
      "have updated the direct listener" in {
        direct should equal(1)
      }
      "have updated the inner listener" in {
        inner should equal(1)
      }
      "have updated the outter listener" in {
        outter should equal(1)
      }
    }
  }
}

object PropertyTester extends PropertyParent with Listenable {
  val parent: PropertyParent = null

  object inner extends PropertyParent with Listenable {
    override def parent = PropertyTester

    val p = Property[Int]()
  }
}