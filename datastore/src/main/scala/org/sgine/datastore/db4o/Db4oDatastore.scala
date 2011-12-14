package org.sgine.datastore.db4o

import com.db4o.ObjectContainer
import org.sgine.datastore.Datastore

import scala.collection.JavaConversions._
import com.db4o.query.Predicate

/**
 * Db4oDatastore is a datastore implementation backed by db4o object database.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class Db4oDatastore(db: ObjectContainer) extends Datastore {
  def persist(obj: AnyRef) = db.store(obj)

  def delete(obj: AnyRef) = db.delete(obj)

  def commit() = db.commit()

  def rollback() = db.rollback()

  def byExample[T](obj: T)(implicit manifest: Manifest[T]) = db.queryByExample[T](obj).toStream

  def all[T]()(implicit manifest: Manifest[T]) = {
    val query = db.query()
    query.constrain(manifest.erasure)
    query.execute[T]().toStream
  }

  def query[T](matcher: (T) => Boolean)(implicit manifest: Manifest[T]) = {
    val predicate = new Predicate[T](manifest.erasure.asInstanceOf[Class[T]]) {
      def `match`(obj: T) = matcher(obj)
    }
    db.query(predicate).toStream
  }

  def close() = db.close()
}