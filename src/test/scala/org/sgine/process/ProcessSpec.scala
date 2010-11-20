package org.sgine.process

import org.scalatest.FlatSpec

import org.scalatest.matchers.ShouldMatchers

import org.sgine.process.Process._

import org.sgine.util.Time

class ProcessSpec extends FlatSpec with ShouldMatchers {
	"Process" should "process a single function asynchronously" in {
		var modified = false
		asynchronous {
			Thread.sleep(10)
			modified = true
		}
		modified should not equal(true)
		Time.waitFor(1.0) {
			modified
		}
		modified should equal(true)
	}
	
	it should "process many tasks asynchronously waiting for processing availability" in {
		val taskCount = 30
		val count = new java.util.concurrent.atomic.AtomicInteger(0)
		for (i <- 0 until taskCount) {
			start {
				Thread.sleep(200)
				count.addAndGet(1)
			}
		}
		count.get should not equal(taskCount)
		Time.waitFor(10.0) {
			count.get == taskCount
		}
		count.get should equal(taskCount)
	}
	
	it should "process many tasks asynchronously attempting" in {
		val taskCount = 30
		val count = new java.util.concurrent.atomic.AtomicInteger(0)
		for (i <- 0 until taskCount) {
			attempt {
				Thread.sleep(200)
				count.addAndGet(1)
			}
		}
		count.get should not equal(taskCount)
		Time.waitFor(10.0) {
			count.get == idealThreadCount
		}
		count.get should equal(idealThreadCount)
	}
	
	it should "process many tasks asynchronously enqueuing overflow" in {
		val taskCount = 30
		val count = new java.util.concurrent.atomic.AtomicInteger(0)
		for (i <- 0 until taskCount) {
			asynchronous {
				Thread.sleep(200)
				count.addAndGet(1)
			}
		}
		count.get should not equal(taskCount)
		Time.waitFor(10.0) {
			count.get == taskCount
		}
		count.get should equal(taskCount)
	}
}