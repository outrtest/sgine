package org.sgine.datastore

import db4o.Db4oDatastore
import neodatis.NeodatisDatastore
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.File

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class DatastoreSpec extends WordSpec with ShouldMatchers {
  "Datastore" when {
    "using db4o" should {
      test("test.database.db4o", Db4oDatastore)
    }
    "using neodatis" should {
      test("test.database.neodatis", NeodatisDatastore)
    }
  }
  
  def test(filename: String, creator: (String) => Datastore) = {
    val dbFile = new File(filename)
    val db = creator(filename)
    val t1 = Test1("test1")
    "create the database file" in {
      dbFile.exists() should equal(true)
    }
    "have no objects in the database" in {
      db.all[Test1]().size should equal(0)
    }
    "insert an object" in {
      db.persist(t1)
    }
    "query the object back out" in {
      val results = db.all[Test1]()
      results.size should equal(1)
      results.head should equal(t1)
    }
    "delete the object" in {
      db.delete(t1)
      db.all[Test1]().size should equal(0)
    }
    "insert an object in a transaction that is rolled back" in {
      val t = Test1("Blah!")
      intercept[RuntimeException] {
        db.transaction {
          db.persist(t)
          db.all[Test1]().size should equal(1)
          throw new RuntimeException("Rollback!")
        }
      }
      db.all[Test1]().size should equal(0)
    }
    "insert five Test1 objects" in {
      val o1 = Test1("One")
      val o2 = Test1("Two")
      val o3 = Test1("Three")
      val o4 = Test1("Four")
      val o5 = Test1("Five")
      db.persistAll(o1, o2, o3, o4, o5)
    }
    "query 'Three' back out" in {
      val results = db.query[Test1] {
        case t if (t.name == "Three") => true
        case _ => false
      }
      results.size should equal(1)
      results.head.name should equal("Three")
    }
    "insert five Test2 objects" in {
      val o1 = Test2("One")
      val o2 = Test2("Two")
      val o3 = Test2("Three")
      val o4 = Test2("Four")
      val o5 = Test2("Five")
      db.persistAll(o1, o2, o3, o4, o5)
    }
    "properly differentiate between class types via all" in {
      db.all[Test1]().size should equal(5)
      db.all[Test2]().size should equal(5)
    }
    "properly differentiate between class types via query" in {
      val results1 = db.query[Test1] {
        case t if (t.name == "Three") => true
        case _ => false
      }
      results1.size should equal(1)
      val results2 = db.query[Test2] {
        case t if (t.name == "Three") => true
        case _ => false
      }
      results2.size should equal(1)
    }
    "insert a Test3 that extends Test2" in {
      db.persist(Test3("Three"))
    }
    "verify Test3 returns with all[Test2]" in {
      db.all[Test2]().size should equal(6)
    }
    "verify Test3 returns with Test2 query" in {
      db.query[Test2]((t: Test2) => true).size should equal(6)
    }
    "close the database" in {
      db.close()
    }
    "delete the database" in {
      dbFile.delete()
      dbFile.exists() should equal(false)
    }
  }
}

case class Test1(name: String)

case class Test2(name: String)

case class Test3(override val name: String) extends Test2(name)