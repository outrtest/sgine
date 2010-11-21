package org.sgine.transaction

import org.scalatest.FlatSpec

import org.scalatest.matchers.ShouldMatchers

import org.sgine.core._

import org.sgine.event.EventHandler
import org.sgine.event.Listenable

import org.sgine.property.NumericProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.util.Time

class TransactionSpec extends FlatSpec with ShouldMatchers {
	val p1 = new NumericProperty(0.0)
	val p2 = new NumericProperty(0.0)
	val p3 = new NumericProperty(0.0)
	
	var changed = false
	
	Listenable.listenTo[PropertyChangeEvent[_]](EventHandler(propertyChanged, ProcessingMode.Blocking), p1, p2, p3)
	
	"Transaction" should "not throw change events until commit" in {
		transaction {
			p1 := 1.0
			changed should not equal(true)
			p2 := 2.0
			changed should not equal(true)
			p3 := 3.0
			changed should not equal(true)
		}
		changed should equal(true)
	}
	
	private def propertyChanged(evt: PropertyChangeEvent[Double]) = {
		println("PROPERTY CHANGED!")
		changed = true
	}
}