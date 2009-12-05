package org.sgine.property

import org.sgine._
import org.sgine.property.adjust._
import org.sgine.property.group._
import org.sgine.work._
import org.sgine.work.unit._

object TestProperties {
	val p = new MutableProperty[Int] with ChangeableProperty[Int] {
		override def changed(oldValue:Int, newValue:Int):Unit = println("Changed: " + oldValue + " to " + newValue)
	}
	val p2 = new MutableProperty[Int] with ListenableProperty[Int]
	val p3 = new MutableProperty[Double] with AdjustableProperty[Double]
	val p4 = new MutableProperty[String] with BindingProperty[String]
	val p5 = new MutableProperty[String] with BindingProperty[String]
	val pg1 = new PropertyGroup {
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
		
		ap1.listeners += ap1Changed _
		ap1 := "One"
		ap1 := "Two"
		p4 bind ap1
		ap1 := "Changed"
		
		println("ap1: " + ap1() + ", p4: " + p4() + ", p5: " + p5())
	}
	
	def p2Changed(evt: PropertyChangeEvent[Int]):Unit = {
		println("p2Changed: " + evt.oldValue + " to " + evt.newValue)
	}
	
	def ap1Changed(evt: PropertyChangeEvent[String]):Unit = {
		println("ap1Changed: " + evt.oldValue + " to " + evt.newValue)
	}
}