package org.sgine.process

import org.scalatest.FlatSpec

import org.scalatest.matchers.ShouldMatchers

import org.sgine.core._

import org.sgine.util.Time

class UpdatableSpec extends FlatSpec with ShouldMatchers {
	"Updatable" should "update exactly five times" in {
		val count = new java.util.concurrent.atomic.AtomicInteger(0)
		update(0.2, 5) {
			count.addAndGet(1)
		}
		Time.waitFor(5.0) {
			count.get == 5
		}
		count.get should equal(5)
	}
	
	it should "invokeLater" in {
		var invoked = false
		invokeLater(1.0) {
			invoked = true
		}
		Thread.sleep(200)
		invoked should not equal(true)
		Time.waitFor(2.0) {
			invoked
		}
		invoked should equal(true)
	}
	
	it should "properly execute" in {
		val value = new java.util.concurrent.atomic.AtomicInteger(0)
		val adder = executor[Int] {(v: Int) => value.addAndGet(v)}
		adder += 3
		adder += 2
		adder += 4
		adder += 1
		
		Time.waitFor(5.0) {
			value.get == 10
		}
		value.get should equal(10)
		
		adder += 5
		
		Time.waitFor(5.0) {
			value.get == 15
		}
		value.get should equal(15)
	}
	
	it should "execute in the right order and synchronously" in {
		var value = "Hello"
		val concatenator = executor[String] {(v: String) => value += v}
		concatenator += " This"
		concatenator += " is"
		concatenator += " in"
		concatenator += " the"
		concatenator += " right"
		Thread.sleep(1000)
		concatenator += " order."
		
		val correct = "Hello This is in the right order."
		Time.waitFor(5.0) {
			value == correct
		}
		value should equal(correct)
	}
}