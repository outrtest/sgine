package org.sgine.property

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.core.ProcessingMode

import org.sgine.event.EventHandler

import org.sgine.property.event.PropertyChangeEvent

class DependentPropertySpec extends FlatSpec with ShouldMatchers {
	val p1 = new AdvancedProperty[String]("One")
	val p2 = new AdvancedProperty[String]("Two", null, null, p1)
	var changed = false
	
	p2.listeners += EventHandler((evt: PropertyChangeEvent[String]) => {
		changed = true
	}, ProcessingMode.Blocking)
	
	"DependentProperty" should "reference the dependent value" in {
		p2() should equal (p1())
	}
	
	it should "not have changed up to this point" in {
		changed should equal (false)
	}
	
	it should "reference the new dependency value" in {
		p1 := "OneChanged"
		p2() should equal ("OneChanged")
	}
	
	it should "throw a PropertyChangeEvent upon dependency change" in {
		changed should equal (true)
	}
	
	it should "reference the changed value upon change of main property" in {
		p2 := "Testing"
		p2() should equal ("Testing")
	}
	
	it should "reference the dependency again after useDependency() is called" in {
		p2.useDependency()
		p2() should equal (p1())
	}
}