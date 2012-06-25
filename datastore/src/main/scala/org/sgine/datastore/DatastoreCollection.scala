package org.sgine.datastore

import event.{DatastoreDelete, DatastorePersist}
import java.util
import org.sgine.event.Listenable

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait DatastoreCollection[T <: Persistable] extends Iterable[T] with Listenable {
  def name: String
  def session: DatastoreSession
  def manifest: Manifest[T]

  final def persist(refs: T*): Unit = {
    refs.foreach {
      case ref => {
        persistInternal(ref)
        fire(DatastorePersist(this, ref))
      }
    }
  }

  final def delete(refs: T*): Unit = {
    refs.foreach {
      case ref => {
        deleteInternal(ref)
        fire(DatastoreDelete(this, ref))
      }
    }
  }

  def byId(id: util.UUID): Option[T]

  def query = DatastoreQuery(collection = this)(manifest)

  protected[datastore] def executeQuery(query: DatastoreQuery[T]): Iterator[T]

  protected def persistInternal(ref: T): Unit

  protected def deleteInternal(ref: T): Unit

  override def toString() = "%s[%s](%s)".format(getClass.getSimpleName, manifest.erasure.getSimpleName, name)
}
