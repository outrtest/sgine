package org.sgine.datastore.neodatis

import scala.collection.JavaConversions._
import org.neodatis.odb.core.query.nq.NativeQuery
import org.neodatis.odb.ODB
import org.sgine.datastore.{Identifiable, Datastore}
import java.util.UUID

/**
 * NeodatisDatastore provides an implementation of a Datastore using Neodatis object database.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class NeodatisDatastore[T <: Identifiable](db: ODB, val layers: Datastore[_]*)(implicit val manifest: Manifest[T]) extends Datastore[T] {
  protected def persistInternal(obj: T) = db.store(obj)

  protected def deleteInternal(id: UUID) = byId(id) match {
    case Some(obj) => {
      db.delete(obj)
      true
    }
    case None => false
  }

  def commit() = db.commit()

  def rollback() = db.rollback()

  def byExample(obj: T) = throw new UnsupportedOperationException("Not implemented")

  def all = query(allResults)

  private val allResults = (obj: T) => true

  override def query(matcher: T => Boolean): Iterator[T] = {
    val query = new NativeQuery() {
      def getObjectType = manifest.erasure

      def `match`(obj: AnyRef) = matcher(obj.asInstanceOf[T])
    }.setPolymorphic(true)
    db.getObjects[T](query).toIterator
  }
}