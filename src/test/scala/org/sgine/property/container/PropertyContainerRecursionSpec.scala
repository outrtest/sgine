package org.sgine.property.container

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.core._

import org.sgine.event._

import org.sgine.property._
import org.sgine.property.event._

class PropertyContainerRecursionSpec extends FlatSpec with ShouldMatchers {
	val t = new Test1()
	var changed = false
	t.inner.listeners += EventHandler(innerChanged, ProcessingMode.Blocking)
	
	private def innerChanged(evt: PropertyChangeEvent[_]) = {
		changed = true
	}
	
	"PropertyContainer" should "not propagate" in {
		t.inner().v := "Hello"
		changed should not equal(true)
	}
}

class Test1 {
	val inner = new AdvancedProperty[Inner](new Inner())
}

class Inner extends PropertyContainer {
	val v = new AdvancedProperty[String](null, this)
}