package org.sgine.process

import org.scalatest.FlatSpec

import org.scalatest.matchers.ShouldMatchers

import org.sgine.process.Process._

class ProcessSpec extends FlatSpec with ShouldMatchers {
	"Process" should "process a single function" in {
		asynchronous {
			println("Hello World!")
		}
		Thread.sleep(1000)
	}
}