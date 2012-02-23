package org.sgine.datastore

//import db4o.Db4oDatastore
//import neodatis.NeodatisDatastore

import db4o.Db4oDatastore
import mongodb.MongoDBDatastore
import neodatis.NeodatisDatastore
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.File
import java.util.UUID
import com.mongodb.casbah.MongoConnection
import com.db4o.Db4oEmbedded
import org.neodatis.odb.ODBFactory

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class DatastoreSpec extends WordSpec with ShouldMatchers {
  "Datastore" when {
    "using db4o" should {
      val file = new File("test.db4o")
      val db = Db4oEmbedded.openFile(file.getPath)
      val db1 = new Db4oDatastore[Test1](db)
      val db2 = new Db4oDatastore[Test2](db)
      val db3 = new Db4oDatastore[Test3](db)
      test(db1, db2, db3) {
        db.close()
        file.delete()
      }
    }
    "using neodatis" should {
      val file = new File("test.neodatis")
      val db = ODBFactory.open(file.getPath)
      val db1 = new NeodatisDatastore[Test1](db)
      val db2 = new NeodatisDatastore[Test2](db)
      val db3 = new NeodatisDatastore[Test3](db)
      test(db1, db2, db3, false) {
        db.close()
        file.delete()
      }
    }
    "using mongodb" should {
      val connection = MongoConnection("localhost", 27017)
      val db = connection("test")
      db.dropDatabase()
      val db1 = new MongoDBDatastore[Test1](db, "test1")
      val db2 = new MongoDBDatastore[Test2](db, "test2")
      val db3 = new MongoDBDatastore[Test3](db, "test3", db2)
      test(db1, db2, db3, false) {
        db.dropDatabase()
        connection.close()
      }
    }
  }

  def test(db1: Datastore[Test1], db2: Datastore[Test2], db3: Datastore[Test3], transactions: Boolean = true)(finish: => Unit) = {
    val t1 = Test1("test1")
    "have no objects in the database" in {
      db1.all.size should equal(0)
    }
    "insert an object" in {
      db1.persist(t1)
    }
    "query the object back out" in {
      val results = db1.all.toList
      results.size should equal(1)
      results.head should equal(t1)
    }
    "delete the object" in {
      db1.delete(t1)
      db1.all.size should equal(0)
    }
    if (transactions) {
      "insert an object in a transaction that is rolled back" in {
        val t = Test1("Blah!")
        intercept[RuntimeException] {
          db1.transaction {
            db1.persist(t)
            db1.all.size should equal(1)
            throw new RuntimeException("Rollback!")
          }
        }
        db1.all.size should equal(0)
      }
    }
    "insert five Test1 objects" in {
      val o1 = Test1("One")
      val o2 = Test1("Two")
      val o3 = Test1("Three")
      val o4 = Test1("Four")
      val o5 = Test1("Five")
      db1.persistAll(o1, o2, o3, o4, o5)
    }
    "query 'Three' back out" in {
      val results = db1.query {
        case t if (t.name == "Three") => true
        case _ => false
      }.toList
      results.size should equal(1)
      results.head.name should equal("Three")
    }
    "insert five Test2 objects" in {
      val o1 = Test2("One")
      val o2 = Test2("Two")
      val o3 = Test2("Three")
      val o4 = Test2("Four")
      val o5 = Test2("Five")
      db2.persistAll(o1, o2, o3, o4, o5)
    }
    "properly differentiate between class types via all" in {
      db1.all.size should equal(5)
      db2.all.size should equal(5)
    }
    "properly differentiate between class types via query" in {
      val results1 = db1.query {
        case t if (t.name == "Three") => true
        case _ => false
      }
      results1.size should equal(1)
      val results2 = db2.query {
        case t if (t.name == "Three") => true
        case _ => false
      }
      results2.size should equal(1)
    }
    "insert a Test3 that extends Test2" in {
      db3.persist(Test3("Three"))
    }
    "verify Test3 returns with all[Test2]" in {
      db2.all.size should equal(6)
    }
    "verify Test3 returns with Test2 query" in {
      db2.query((t: Test2) => true).size should equal(6)
    }
    "close resources in" in {
      finish
    }
  }
}

case class Test1(name: String, id: UUID = UUID.randomUUID()) extends Identifiable

case class Test2(name: String, id: UUID = UUID.randomUUID()) extends Identifiable

case class Test3(override val name: String, override val id: UUID = UUID.randomUUID()) extends Test2(name, id)