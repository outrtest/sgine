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
	val pool = java.util.concurrent.Executors.newFixedThreadPool(1)
	
	val p1 = new NumericProperty(0.0)
	val p2 = new NumericProperty(0.0)
	val p3 = new NumericProperty(0.0)
	
	var changed = false
	
	Listenable.listenTo[PropertyChangeEvent[_]](EventHandler(propertyChanged, ProcessingMode.Blocking), p1, p2, p3)
	
	"Transaction" should "not throw change events until commit" in {
		transaction {
			p1 := 1.0
			changed should not equal(true)
			p1() should equal(1.0)
			getThreaded(p1) should equal(0.0)
			p2 := 2.0
			changed should not equal(true)
			p2() should equal(2.0)
			getThreaded(p2) should equal(0.0)
			p3 := 3.0
			changed should not equal(true)
			p3() should equal(3.0)
			getThreaded(p3) should equal(0.0)
		}
		changed should equal(true)
	}
	
	it should "allow committing of individual values" in {
		changed = false
		
		transaction {
			p2 := 5.0
			p2() should equal(5.0)
			getThreaded(p2) should equal(2.0)
			changed should not equal(true)
			p2.uncommitted should equal(true)
			
			p2.commit()
			p2() should equal(5.0)
			getThreaded(p2) should equal(5.0)
			changed should equal(true)
			p2.uncommitted should equal(false)
		}
	}
	
	it should "support reverting of properties" in {
		changed = false
		
		transaction {
			p2 := 8.0
			p2() should equal(8.0)
			getThreaded(p2) should equal(5.0)
			changed should equal(false)
			p2.uncommitted should equal(true)
			
			p2.revert()
			p2() should equal(5.0)
			getThreaded(p2) should equal(5.0)
			changed should not equal(true)
			p2.uncommitted should equal(false)
		}
	}
	
	it should "auto rollback upon exception" in {
		changed = false
		
		try {
			transaction {
				p3 := 6.0
				p3() should equal(6.0)
				getThreaded(p3) should equal(3.0)
				changed should equal(false)
				p3.uncommitted should equal(true)
				
				throw new TestException
			}
		} catch {
			case exc: TestException => // Ignore
		}
		
		p3() should equal(3.0)
		getThreaded(p3) should equal(3.0)
		changed should equal(false)
		p3.uncommitted should equal(false)
	}
	
	it should "shutdown pool" in {
		pool.shutdown()
	}
	
	private def propertyChanged(evt: PropertyChangeEvent[Double]) = {
		changed = true
	}
	
	private def getThreaded(p: NumericProperty) = pool.submit(new java.util.concurrent.Callable[Double]() {
		def call() = p()
	}).get
	
	class TestException extends RuntimeException
}