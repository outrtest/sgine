package org.sgine.property.properties

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.sgine.property.{ImmutableProperty, MutableProperty}

/**
 * Specification / test for properties package classes.
 */
class PropertiesSpec extends FlatSpec with ShouldMatchers {
	val props = new StaticAndDynamicProperties {
		val name = new ImmutableProperty("Igor")
		val hitPoints = new MutableProperty(100f)

		addProperty('mana, new MutableProperty(100f))
	}

	"StaticProperties" should "find property 'name' correctly" in {
		props.containsProperty('name) should be (true)
	}
	
	it should "find property 'hitpoints' correctly" in {
		props.containsProperty('hitPoints) should be (true)
	}
	
	"DynamicProperties" should "find property 'mana' correctly" in {
		props.containsProperty('mana) should be (true)
	}
}



