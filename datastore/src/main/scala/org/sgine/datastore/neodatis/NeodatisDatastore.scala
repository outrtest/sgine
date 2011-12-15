package org.sgine.datastore.neodatis

import org.sgine.datastore.Datastore
import scala.collection.JavaConversions._
import org.neodatis.odb.core.query.nq.NativeQuery
import org.neodatis.odb.{ODBFactory, ODB}

/**
 * NeodatisDatastore provides an implementation of a Datastore using Neodatis object database.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class NeodatisDatastore(dbFunction: => ODB) extends Datastore {
  private var db: ODB = dbFunction
  
  def persist(obj: AnyRef) = db.store(obj)

  def delete(obj: AnyRef) = db.delete(obj)

  def commit() = db.commit()

  def rollback() = {
    db.rollback()
    db.close()
    db = dbFunction
  }

  def byExample[T](obj: T)(implicit manifest: Manifest[T]) = throw new UnsupportedOperationException("Not implemented")

  def all[T]()(implicit manifest: Manifest[T]) = query[T](allResults.asInstanceOf[(T) => Boolean])

  def query[T](matcher: (T) => Boolean)(implicit manifest: Manifest[T]) = {
    val query = new NativeQuery() {
      def getObjectType = manifest.erasure

      def `match`(obj: AnyRef) = matcher(obj.asInstanceOf[T])
    }.setPolymorphic(true)
    db.getObjects[T](query).toStream
  }
  
  private val allResults = (obj: AnyRef) => true

  def close() = db.close()
}

object NeodatisDatastore extends Function1[String, Datastore] {
  def apply(filename: String): NeodatisDatastore = apply(ODBFactory.open(filename))

  def apply(dbFunction: => ODB) = new NeodatisDatastore(dbFunction)
}