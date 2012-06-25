package org.sgine.datastore.event

import org.sgine.event.Event
import org.sgine.datastore.{DatastoreCollection, Persistable}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait DatastoreEvent[T <: Persistable] extends Event {
  def obj: T

  def collection: DatastoreCollection[T]
}

case class DatastorePersist[T <: Persistable](collection: DatastoreCollection[T], obj: T) extends DatastoreEvent[T]

case class DatastoreDelete[T <: Persistable](collection: DatastoreCollection[T], obj: T) extends DatastoreEvent[T]