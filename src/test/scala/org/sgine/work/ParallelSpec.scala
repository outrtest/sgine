package org.sgine.work

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.log._

import org.sgine.util.Timer

import org.sgine.work._

class ParallelSpec extends FlatSpec with ShouldMatchers {
	"ParallelIterable" should "process 100 jobs in less than 5 seconds" in {
		val range = 0 until 100
		val timer = Timer()
		var increment = 0
		timer.start("parallel")
		range.foreachParallel((i: Int) => {
			Thread.sleep(500)
			range.synchronized {
				increment += 1
			}
		})
		timer.stop("parallel")
		timer.get("parallel").toInt should be <= (5000)
		increment should equal (100)
	}
}