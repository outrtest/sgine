package org.sgine.property

import org.sgine._

import org.sgine.log._

import org.sgine.property.animate._
import org.sgine.property.container._
import org.sgine.property.event._

import org.sgine.work._
import org.sgine.work.unit._

object TestProperties {
	val p = new MutableProperty[Int] with ChangeableProperty[Int] {
		override def changed(oldValue:Int, newValue:Int):Unit = info("Changed: " + oldValue + " to " + newValue)
	}
	val p2 = new MutableProperty[Int] with ListenableProperty[Int]
	val p3 = new MutableProperty[Double] with AnimatingProperty[Double]
	val p4 = new MutableProperty[String] with BindingProperty[String]
	val p5 = new MutableProperty[String] with BindingProperty[String]
	val pg1 = new PropertyContainer {
		val p1 = new MutableProperty[Int]
		val p2 = new MutableProperty[String]
	}
	val ap1 = new AdvancedProperty[String]
	
	def main(args:Array[String]) = {
		p(5)
		p(8, false)
		p(10)
		
		p2.listeners += p2Changed _
		p2(2)
		p2(4)
		
		p3 := 0.0
		p3.animator = new LinearNumericAnimator(5.0)
		p3 := 50.0
		val time = System.currentTimeMillis
		info("Current: " + p3())
		Thread.sleep(1000)
		info("Delayed1: " + p3())
		Thread.sleep(1000)
		info("Delayed2: " + p3())
		p3.waitForTarget()
		info("Done: " + p3() + " in " + (System.currentTimeMillis - time) + "ms")
		
		p4 := "One"
		p5 := "Two"
		p5 bind p4
		p4 := "Three"
		info("p4: " + p4() + ", p5: " + p5())
		
		info("PropertyContainer: " + pg1.properties.size)
		
		ap1.listeners += ap1Changed _
		ap1 := "One"
		ap1 := "Two"
		p4 bind ap1
		ap1 := "Changed"
		
		info("ap1: " + ap1() + ", p4: " + p4() + ", p5: " + p5())
	}
	
	def p2Changed(evt: PropertyChangeEvent[Int]):Unit = {
		info("p2Changed: " + evt.oldValue + " to " + evt.newValue)
	}
	
	def ap1Changed(evt: PropertyChangeEvent[String]):Unit = {
		info("ap1Changed: " + evt.oldValue + " to " + evt.newValue)
	}
}