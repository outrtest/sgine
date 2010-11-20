package org.sgine.process

import org.scalatest.FlatSpec

import org.scalatest.matchers.ShouldMatchers

class UpdatableSpec extends FlatSpec with ShouldMatchers {
	"Updatable" should "update" in {
		update(1.0) {
			println("Test")
		}
		Thread.sleep(5000)
	}
}