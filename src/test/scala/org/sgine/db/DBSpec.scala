package org.sgine.db

import java.io.File

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.log._

import org.sgine.property.AdvancedProperty

class DBSpec extends FlatSpec with ShouldMatchers {
	var db: DB = _
	var transaction: Transaction = _
	
	val file = new File("test.db")
	file.delete()
	
	"DB" should "not exist" in {
		file.exists should equal(false)
	}
	
	it should "create a file on disk" in {
		db = DB.open(file)
		file.exists should equal(true)
	}
	
	it should "have a valid instance of a db" in {
		db should not equal(null)
	}
	
	it should "create a valid transaction" in {
		transaction = db.transaction()
		transaction should not equal(null)
	}
	
	it should "store a TestObject with id 1" in {
		transaction.store(new TestObject(1, "Test 1"))
	}
	
	it should "store a TestObject with id 2" in {
		transaction.store(new TestObject(2, "Test 2"))
	}
	
	it should "store a TestObject2" in {
		transaction.store(new TestObject2)
	}
	
	it should "commit the transaction" in {
		transaction.commit()
	}
	
	it should "find a single instance of TestObject id: 1" in {
		transaction.query((t: TestObject) => t.id == 1).toList.size should equal (1)
	}
	
	it should "find a two instances of TestObject id: 2" in {
		transaction.query((t: TestObject) => t.id == 2).toList.size should equal (2)
	}
	
	it should "remove all TestObject's with id: 2" in {
		for (t <- transaction.query((t: TestObject) => t.id == 2)) {
			transaction.delete(t)
		}
		transaction.query((t: TestObject) => t.id == 2).toList.size should equal (0)
	}
	
	it should "rollback the previous transaction" in {
		transaction.rollback()
	}
	
	it should "now have two TestObject's with id: 2" in {
		transaction.query((t: TestObject) => t.id == 2).toList.size should equal (2)
	}
	
	it should "properly store an instance of TestProperties" in {
		val tp = new TestProperties()
		tp.name := "One"
		
		transaction.store(tp)
	}
	
	it should "properly retrieve an instance of TestProperties" in {
		val tp = transaction.find(classOf[TestProperties])
		tp should not equal(None)
		tp.get.name() should equal("One")
	}
	
	it should "close the transaction" in {
		transaction.close()
	}
	
	it should "close the db on disk" in {
		db.close()
	}
	
	it should "delete the db file" in {
		file.delete() should equal(true)
	}
}

class TestObject(val id: Int, val name: String)

class TestObject2 extends TestObject(2, "TO2")

class TestProperties {
	val name = new AdvancedProperty[String]("")
}