package org.sgine.datastore.event

import org.sgine.event.Event
import org.sgine.datastore.{Datastore, Identifiable}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait DatastoreEvent[T <: Identifiable] extends Event {
  def obj: T

  def datastore: Datastore[T]

  def target = datastore
}

case class DatastorePersist[T <: Identifiable](datastore: Datastore[T], obj: T) extends DatastoreEvent[T]

case class DatastoreReplace[T <: Identifiable](datastore: Datastore[T], obj: T) extends DatastoreEvent[T]

case class DatastoreDelete[T <: Identifiable](datastore: Datastore[T], obj: T) extends DatastoreEvent[T]