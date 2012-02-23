package org.sgine.datastore.db4o


import scala.collection.JavaConversions._
import com.db4o.query.Predicate
import com.db4o.ObjectContainer
import org.sgine.datastore.{Identifiable, Datastore}
import java.util.UUID

/**
 * Db4oDatastore is a datastore implementation backed by db4o object database.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Db4oDatastore[T <: Identifiable](db: ObjectContainer, val layers: Datastore[_]*)(implicit val manifest: Manifest[T]) extends Datastore[T] {
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

  def byExample(obj: T) = db.queryByExample[T](obj).toIterator

  def all = {
    val query = db.query()
    query.constrain(manifest.erasure)
    query.execute[T]().toIterator
  }

  override def query(matcher: T => Boolean): Iterator[T] = {
    val predicate = new Predicate[T](manifest.erasure.asInstanceOf[Class[T]]) {
      def `match`(obj: T) = matcher(obj)
    }
    db.query(predicate).toIterator
  }
}