package org.sgine.datastore

//import db4o.Db4oDatastore
//import neodatis.NeodatisDatastore

//import db4o.Db4oDatastore
import impl.mongodb.MongoDBDatastore
//import mongodb.MongoDBDatastore
//import neodatis.NeodatisDatastore
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import java.util.UUID
import org.sgine.Precision

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class DatastoreSpec extends WordSpec with ShouldMatchers {
  "Datastore" when {
//    "using db4o" should {
//      val file = new File("test.db4o")
//      val db = Db4oEmbedded.openFile(file.getPath)
//      val db1 = new Db4oDatastore[Test1](db)
//      val db2 = new Db4oDatastore[Test2](db)
//      val db3 = new Db4oDatastore[Test3](db)
//      val db4 = new Db4oDatastore[Test4](db)
//      test(db1, db2, db3, db4) {
//        db.close()
//        file.delete()
//      }
//    }
//    "using neodatis" should {
//      val file = new File("test.neodatis")
//      val db = ODBFactory.open(file.getPath)
//      val db1 = new NeodatisDatastore[Test1](db)
//      val db2 = new NeodatisDatastore[Test2](db)
//      val db3 = new NeodatisDatastore[Test3](db)
//      val db4 = new NeodatisDatastore[Test4](db)
//      test(db1, db2, db3, db4, false) {
//        db.close()
//        file.delete()
//      }
//    }
    "using mongodb" should {
      val datastore = new MongoDBDatastore("localhost", 27017, "DatastoreSpec")
      datastore.globalize = true
      val (session, created) = datastore.createOrGet()
      datastore.session.dropDatabase()
      test(session) {
        if (created) {
          datastore.session.dropDatabase()
          datastore.disconnect()
        }
      }
    }
  }

  def test(session: DatastoreSession)(finish: => Unit) = {
    val db1 = session[Test1]
    val db2 = session[Test2]
    val db3 = session[Test3]
    val db4 = session[Test4]
    println("Within the session...")
    val t1 = Test1("test1")
    "have no objects in the database" in {
      println("Checking db size...")
      db1.size should equal(0)
    }
    "insert an object" in {
      db1.persist(t1)
    }
    "query the object back out" in {
      val results = db1.toList
      results.size should equal(1)
      results.head should equal(t1)
    }
    "delete the object" in {
      db1.delete(t1)
      db1.size should equal(0)
    }
    "insert five Test1 objects" in {
      val o1 = Test1("One")
      val o2 = Test1("Two")
      val o3 = Test1("Three")
      val o4 = Test1("Four")
      val o5 = Test1("Five")
      db1.persist(o1, o2, o3, o4, o5)
    }
    "query 'Three' back out" in {
      val query = db1.query.filter(Test1.name equal "Three")
      val results = query.toList
      results.size should equal(1)
      results.head.name should equal("Three")
    }
    "insert five Test2 objects" in {
      val o1 = Test2("One")
      val o2 = Test2("Two")
      val o3 = Test2("Three")
      val o4 = Test2("Four")
      val o5 = Test2("Five")
      db2.persist(o1, o2, o3, o4, o5)
    }
    "properly differentiate between class types via all" in {
      db1.size should equal(5)
      db2.size should equal(5)
    }
    "property differentiate between class types via query" in {
      val results1 = db1.query.filter(Test1.name equal "Three").toList
      val results2 = db2.query.filter(Test2.name equal "Three").toList
      results1.size should equal(1)
      results2.size should equal(1)
    }
    "insert a Test3 that extends Test2" in {
      db3.persist(Test3("Three"))
    }
//    "verify Test3 returns with all[Test2]" in {
//      db2.size should equal(6)
//    }
//    "verify Test3 returns with Test2 query" in {
//      db2.query((t: Test2) => true).size should equal(6)
//    }
    "persist a Test4 with an EnumEntry" in {
      db4.persist(Test4("first", Precision.Milliseconds))
    }
//    "query back one Test4 with an EnumEntry" in {
//      val t = db4.first()
//      t.name should equal("first")
//      t.precision should equal(Precision.Milliseconds)
//    }
    "close resources in" in {
      finish
    }
  }
}

case class Test1(name: String, id: UUID = UUID.randomUUID()) extends Persistable

object Test1 {
  val name = Field[Test1, String]("name")
  val id = Field.id[Test1]
}

case class Test2(name: String, id: UUID = UUID.randomUUID()) extends Persistable

object Test2 {
  val name = Field[Test2, String]("name")
  val id = Field.id[Test2]
}

case class Test3(override val name: String, override val id: UUID = UUID.randomUUID()) extends Test2(name, id)

object Test3 {
  val name = Field[Test3, String]("name")
  val id = Field.id[Test3]
}

case class Test4(name: String, precision: Precision, id: UUID = UUID.randomUUID()) extends Persistable

object Test4 {
  val name = Field[Test4, String]("name")
  val precision = Field[Test4, Precision]("precision")
  val id = Field.id[Test4]
}