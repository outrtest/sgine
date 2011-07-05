package org.sgine.property

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.sgine.transaction._
import org.sgine.concurrent.Executor

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 7/3/11
 */
class MutablePropertySpec extends FlatSpec with ShouldMatchers {
  "MutableProperties" should "default properly" in {
    val p1 = new MutableProperty[String]
    val p2 = new MutableProperty[Int]
    p1.value should equal(null)
    p2.value should equal(0)
  }

  it should "support initial values" in {
    val p1 = new MutableProperty[String]("initial value")
    p1.value should equal("initial value")
    val p2 = new MutableProperty[Int](5)
    p2.value should equal(5)
  }

  it should "throw an event on change" in {
    val p = new MutableProperty[String]
    var changed: String = null
    p.change.synchronous {
      case event => changed = event.newValue
    }
    val setValue = "Test"
    changed should equal(null)
    p.value = setValue
    changed should equal(setValue)
  }

  it should "support default" in {
    val p = new MutableProperty[String]
    p.defaultFunction = () => "Defaulted!"
    p.value should equal("Defaulted!")
    p.value = "Not Default!"
    p.value should equal("Not Default!")
    p.default = true
    p.value should equal("Defaulted!")
    p.default = false
    p.value should equal("Not Default!")
  }

  it should "support delegation" in {
    val p = new MutableProperty[String]
    var value = "Delegated!"
    val getter = () => value
    val setter = (s: String) => value = s
    p.delegate(getter, setter)
    p.value should equal("Delegated!")
    p.value = "New Value!"
    p.value should equal("New Value!")
    value should equal("New Value!")
  }

  it should "support binding and unbinding" in {
    val p1 = new MutableProperty[String]("One")
    val p2 = new MutableProperty[String]("Two")
    p1.value should equal("One")
    p2.value should equal("Two")
    p1 bind p2
    p1.value should equal("One")
    p2.value should equal("Two")
    p1.value = "One"
    p1.value should equal("One")
    p2.value should equal("Two")
    p2.value = "Dos"
    p1.value should equal("Dos")
    p2.value should equal("Dos")
    p2.change.size should equal(1)
    p1 unbind p2
    p2.change.size should equal(0)
    p2.value = "Two"
    p1.value should equal("Dos")
    p2.value should equal("Two")
  }

  it should "support transactions" in {
    val p = new MutableProperty[String]("Initial")
    transaction {
      p.value = "In Transaction"
      p.value should equal("In Transaction")
      val other = Executor.invokeFuture {
        p.value
      }.get
      other should equal("Initial")
    }
  }

  it should "filter values" in {
    val p = new MutableProperty[String]
    p.filter = (value: String) => value.reverse
    p.value = "olleH"
    p.value should equal("Hello")
  }
}