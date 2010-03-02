package org.sgine.property

import org.sgine.event._
import org.sgine.property.event._

object TestPropertyTransactions {
	val p = new AdvancedProperty[String]("Initial")
	
	def main(args: Array[String]): Unit = {
		p.listeners += EventHandler(events, processingMode = ProcessingMode.Blocking)
		test()
		p := "New"
		test()
		p.revert()
		test()
		p := "Changed"
		test()
		p.commit()
		test()
		p := "Changed2"
		test()
		p := "Changed"
		test()
	}
	
	def test() = {
		println("Original: " + p.originalValue)
		println("Current: " + p())
		println("Uncommitted: " + p.uncommitted)
		println("----------------------------------")
	}
	
	def events(evt: PropertyTransactionEvent[String]) = {
		println("Event: " + evt.oldValue + " to " + evt.newValue + " " + (if (evt.isCommit) "commit" else "revert"))
	}
}