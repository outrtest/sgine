package org.sgine.property.properties

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.sgine.property.{ImmutableProperty, MutableProperty}

/**
 * Specification / test for properties package classes.
 */
class PropertiesSpec extends FlatSpec with ShouldMatchers {

	"StaticProperties" should "find properties correctly" in {
		val igor = new TestClass()
		igor.containsProperty('name) should be (true)
		igor.containsProperty('hitPoints) should be (true)
		igor.containsProperty('mana) should be (true)
	}

	class TestClass extends StaticAndDynamicProperties {
		val name = new ImmutableProperty("Igor")
		val hitPoints = new MutableProperty(100f)

		addProperty('mana, new MutableProperty(100f))
	}
}



