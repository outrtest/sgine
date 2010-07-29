package org.sgine.db

import java.io.File

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.log._

class DBSpec extends FlatSpec with ShouldMatchers {
	var db: DB = _
	
	val file = new File("test.db")
	file.delete()
	
	"DB" should "not exist" in {
		file.exists should equal(false)
	}
	
	it should "create a file on disk" in {
		db = DB.open("test", file)
		file.exists should equal(true)
	}
	
	it should "have a valid instance of a db" in {
		db should not equal(null)
	}
	
	it should "store a TestObject" in {
		db store TestObject(1, "Testing")
	}
	
	it should "commit the transaction" in {
		db commit
	}
	
	it should "find TestObject id: 1" in {
//		db find(t: TestObject => t.id == 1) should not equal(None)
	}
	
	it should "close the db on disk" in {
		DB.close("test")
	}
	
	it should "delete the db file" in {
		file.delete() should equal(true)
	}
}

case class TestObject(id: Int, name: String)