package org.sgine.datastore

import db4o.Db4oDatastore
import neodatis.NeodatisDatastore
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import com.db4o.Db4oEmbedded
import java.io.File
import org.neodatis.odb.ODBFactory

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class DatastoreSpec extends WordSpec with ShouldMatchers {
  "Datastore" when {
    "using db4o" should {
      val dbFile = new File("test.database.db4o")
      val db = Db4oDatastore(Db4oEmbedded.openFile(dbFile.getAbsolutePath))
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
      "close the database" in {
        db.close()
      }
      "delete the database" in {
        dbFile.delete()
        dbFile.exists() should equal(false)
      }
    }
    "using neodatis" should {
      val dbFile = new File("test.database.neodatis")
      val db = NeodatisDatastore(ODBFactory.open(dbFile.getAbsolutePath))
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
      "close the database" in {
        db.close()
      }
      "delete the database" in {
        dbFile.delete()
        dbFile.exists() should equal(false)
      }
    }
  }
}

case class Test1(name: String)