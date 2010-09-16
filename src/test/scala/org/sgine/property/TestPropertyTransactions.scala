package org.sgine.property

import org.sgine.core.ProcessingMode

import org.sgine.event._

import org.sgine.log._

import org.sgine.property.event._

object TestPropertyTransactions {
	val p = new AdvancedProperty[String]("Initial") with TransactionalProperty[String]
	
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
		info("Original: " + p.originalValue)
		info("Current: " + p())
		info("Uncommitted: " + p.uncommitted)
		info("----------------------------------")
	}
	
	def events(evt: PropertyTransactionEvent[String]) = {
		info("Event: " + evt.oldValue + " to " + evt.newValue + " " + (if (evt.isCommit) "commit" else "revert"))
	}
}