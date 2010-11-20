package org.sgine.process

import org.scalatest.FlatSpec

import org.scalatest.matchers.ShouldMatchers

import org.sgine.util.Time

class UpdatableSpec extends FlatSpec with ShouldMatchers {
	"Updatable" should "update" in {
		val count = new java.util.concurrent.atomic.AtomicInteger(0)
		update(0.2, 5) {
			count.addAndGet(1)
		}
		Time.waitFor(5.0) {
			count.get == 5
		}
		count.get should equal(5)
	}
}