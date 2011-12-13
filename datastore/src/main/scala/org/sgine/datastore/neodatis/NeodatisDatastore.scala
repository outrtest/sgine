package org.sgine.datastore.neodatis

import org.neodatis.odb.ODB
import org.sgine.datastore.Datastore
import scala.collection.JavaConversions._
import org.neodatis.odb.core.query.nq.SimpleNativeQuery

/**
 * NeodatisDatastore provides an implementation of a Datastore using Neodatis object database.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class NeodatisDatastore(db: ODB) extends Datastore {
  def persist(obj: AnyRef) = db.store(obj)

  def delete(obj: AnyRef) = db.delete(obj)

  def commit() = db.commit()

  def rollback() = db.rollback()

  def byExample[T](obj: T)(implicit manifest: Manifest[T]) = throw new UnsupportedOperationException("Not implemented")

  def all[T]()(implicit manifest: Manifest[T]) = db.getObjects[T](manifest.erasure).toStream

  def query[T](matcher: (T) => Boolean)(implicit manifest: Manifest[T]) = {
    val query = new SimpleNativeQuery() {
      def `match`(obj: AnyRef) = {
        if (manifest.erasure.isAssignableFrom(obj.getClass)) {
          matcher(obj.asInstanceOf[T])
        } else {
          false
        }
      }
    }
    db.getObjects[T](query).toStream
  }

  def close() {}
}