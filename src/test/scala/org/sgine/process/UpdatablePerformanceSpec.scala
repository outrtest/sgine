package org.sgine.process

import org.scalatest.FlatSpec

import org.scalatest.matchers.ShouldMatchers

import org.sgine.core._

import org.sgine.process.updatable._

import org.sgine.util.Time

class UpdatablePerformanceSpec extends FlatSpec with ShouldMatchers {
	"Updatable" should "update as many times as possible" ignore {
		val updatables = for (i <- 0 until 100) yield new TestUpdatable()
		Thread.sleep(10000)
		val total = updatables.foldLeft(0)((current: Int, updatable: TestUpdatable) => current + updatable.updated)
		updatables.foreach(_.die())
		println("Updated: " + total + " times in 10 seconds.")
	}
}

class TestUpdatable extends TimedUpdatable {
	val rate = 0.001
	@volatile var updated: Int = 0
	
	def update(time: Double) = {
		updated += 1
	}
	
	override def die() = super.die()
	
	start()
}