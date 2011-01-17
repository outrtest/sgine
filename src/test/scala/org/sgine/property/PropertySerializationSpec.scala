package org.sgine.property

import container.PropertyContainer
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import java.io._

/**
 * PropertySerializationSpec tests the ability to serialize and restore properties
 * with the built-in serialization framework.
 *
 * @author mhicks
 * Date: 1/13/11
 * Time: 12:27 PM
 */
class PropertySerializationSpec extends FlatSpec with ShouldMatchers {
  val apf = new File("ap.bin")
  val tcf = new File("tc.bin")

  "AdvancedProperty" should "serialize successfully" in {
    val p = new AdvancedProperty[String]("Hello")
    val oos = new ObjectOutputStream(new FileOutputStream(apf))
    try {
      oos.writeObject(p)
    } finally {
      oos.close()
      oos.flush()
    }
  }

  it should "restore the property" in {
    val ois = new ObjectInputStream(new FileInputStream(apf))
    try {
      val p = ois.readObject().asInstanceOf[AdvancedProperty[String]]
      p() should equal("Hello")
    } finally {
      ois.close()
    }
  }

  it should "clean up after itself" in {
    if (!apf.delete()) apf.deleteOnExit()
  }

  "PropertyContainer" should "serialize successfully" in {
    val t = new TestContainer()
    t.s := "Goodbye"
    t.i := 26
    val oos = new ObjectOutputStream(new FileOutputStream(tcf))
    try {
      oos.writeObject(t)
    } finally {
      oos.close()
      oos.flush()
    }
  }

  it should "restore properly" in {
    val ois = new ObjectInputStream(new FileInputStream(tcf))
    try {
      val t = ois.readObject().asInstanceOf[TestContainer]
      t.s() should equal("Goodbye")
      t.i() should equal(26)
    } finally {
      ois.close()
    }
  }

  it should "clean up after itself" in {
    if (!tcf.delete()) tcf.deleteOnExit()
  }
}

class TestContainer extends PropertyContainer {
  val s = new AdvancedProperty[String]("", this)
  val i = new NumericProperty(0, this)
}