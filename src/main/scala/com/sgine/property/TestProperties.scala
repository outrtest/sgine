package com.sgine.property

import com.sgine._
import com.sgine.property.adjust._
import com.sgine.property.group._
import com.sgine.work._
import com.sgine.work.unit._

object TestProperties {
	val p = new MutableProperty[Int] with ChangeableProperty[Int] {
		def changed(oldValue:Int, newValue:Int):Unit = println("Changed: " + oldValue + " to " + newValue)
	}
	val p2 = new MutableProperty[Int] with ListenableProperty[Int]
	val p3 = new MutableProperty[Double] with AdjustableProperty[Double]
	val p4 = new MutableProperty[String] with BindingProperty[String]
	val p5 = new MutableProperty[String] with BindingProperty[String]
	val pg1 = new PropertyGroup {
		val p1 = new MutableProperty[Int]
		val p2 = new MutableProperty[String]
	}
	
	def main(args:Array[String]) = {
		p(5)
		p(8, false)
		p(10)
		
		p2.listeners.add(p2Changed)
		p2(2)
		p2(4)
		
		p3 := 0.0
		val updater = new Updater(p3)							// Must keep a reference lest it go away
		DefaultWorkManager += new RepeatingUnit(updater)
		p3.adjuster = new LinearNumericAdjuster(5.0)
		p3 := 50.0
		val time = System.currentTimeMillis
		println("Current: " + p3())
		Thread.sleep(1000)
		println("Delayed1: " + p3())
		Thread.sleep(1000)
		println("Delayed2: " + p3())
		p3.waitForTarget()
		println("Done: " + p3() + " in " + (System.currentTimeMillis - time) + "ms")
		
		p4 := "One"
		p5 := "Two"
		p5 bind p4
		p4 := "Three"
		println("p4: " + p4() + ", p5: " + p5())
		
		println("PropertGroup: " + pg1.properties.length)
	}
	
	def p2Changed(p:Property[Int], oldValue:Int, newValue:Int):Unit = {
		println("p2Changed: " + oldValue + " to " + newValue)
	}
}