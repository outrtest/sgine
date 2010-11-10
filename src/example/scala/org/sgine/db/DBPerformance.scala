package org.sgine.db

import java.io.File

import org.sgine.core._

import org.sgine.log._

import org.sgine.property.AdvancedProperty

object DBPerformance {
	def main(args: Array[String]): Unit = {
		val file = new File("example.db")
//		file.delete()
		
		val db = DB.open(file)
		val transaction = db.transaction()
		
//		transaction.store(new TestEnums(1, "left", "top"))
//		transaction.store(new TestEnums(2, "center", "top"))
//		transaction.store(new TestEnums(3, "right", "top"))
//		transaction.store(new TestEnums(4, "left", "middle"))
//		transaction.store(new TestEnums(5, "center", "middle"))
//		transaction.store(new TestEnums(6, "right", "middle"))
//		transaction.store(new TestEnums(7, "left", "bottom"))
//		transaction.store(new TestEnums(8, "center", "bottom"))
//		transaction.store(new TestEnums(9, "right", "bottom"))
//		transaction.commit()
		
		def test(id: Int, halign: HorizontalAlignment, valign: VerticalAlignment) = {
			val option = transaction.find((te: TestEnums) => te.id() == id)
			val te = option.get
			if (te.halign() != halign) {
				System.err.println("Expected: " + halign + ", Received: " + te.halign())
			}
			if (te.valign() != valign) {
				System.err.println("Expected: " + valign + ", Received: " + te.valign())
			}
		}
		test(1, "left", "top")
		test(2, "center", "top")
		test(3, "right", "top")
		test(4, "left", "middle")
		test(5, "center", "middle")
		test(6, "right", "middle")
		test(7, "left", "bottom")
		test(8, "center", "bottom")
		test(9, "right", "bottom")
		
		db.close()
	}
}