package org.sgine.datastore

import event.{DatastorePersist, DatastoreDelete}
import org.sgine.event.Listenable
import java.util.UUID

/**
 * Datastore implements functionality for working with a backing datastore.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Datastore[T <: Identifiable] extends Listenable {
  def manifest: Manifest[T]

  def layers: Seq[Datastore[_]]

  protected def persistInternal(obj: T): Unit

  protected def deleteInternal(id: UUID): Boolean

  def transaction(f: => Unit) = try {
    f
    commit()
  } catch {
    case throwable => {
      rollback()
      throw throwable
    }
  }

  def canUse(obj: AnyRef) = manifest.erasure.isAssignableFrom(obj.getClass)

  def persist(obj: T): Unit = {
    persistInternal(obj)
    fire(DatastorePersist(this, obj))

    layers.foreach(layer => if (layer.canUse(obj)) {
      layer.asInstanceOf[Datastore[T]].persist(obj)
    })
  }

  /**
   * Removes the object by the same id in the datastore before persisting this one.
   *
   * @param obj to persist
   */
  def update(obj: T): Unit = {
    byId(obj.id) match {
      case Some(instance) => delete(instance)
      case None =>
    }
    persist(obj)

    layers.foreach(layer => if (layer.canUse(obj)) {
      layer.asInstanceOf[Datastore[T]].update(obj)
    })
  }

  def persistAll(objects: T*) = objects.foreach(persist)

  def delete(obj: T): Boolean = if (deleteInternal(obj.id)) {
    fire(DatastoreDelete(this, obj))
    layers.foreach(layer => if (layer.canUse(obj)) {
      layer.asInstanceOf[Datastore[T]].delete(obj)
    })
    true
  } else {
    false
  }

  def clear() = all.foreach(obj => delete(obj))

  def commit(): Unit

  def rollback(): Unit

  def byExample(obj: T): Iterator[T]

  def byId(id: UUID): Option[T] = firstOption(obj => obj.id == id)

  def deleteById(id: UUID) = {
    byId(id) match {
      case Some(instance) => delete(instance)
      case None => false
    }
  }

  def all: Iterator[T]

  /**
   * Queries the store for entries matching 'matcher' as a criteria.
   *
   * NOTE: For performance reasons this method should generally be overridden by implementations.
   *
   * @param matcher is used to limit the results
   * @return Iterator[T]
   */
  def query(matcher: T => Boolean): Iterator[T] = all.filter(matcher)

  def firstByExample(obj: T) = byExample(obj) match {
    case results if (results.isEmpty) => null.asInstanceOf[T]
    case results => results.next()
  }

  def firstOptionByExample(obj: T) = firstByExample(obj) match {
    case null => None
    case result => Some(result)
  }

  def first() = all match {
    case results if (results.isEmpty) => null.asInstanceOf[T]
    case results => results.next()
  }

  def firstOption() = all match {
    case results if (results.isEmpty) => None
    case results => Some(results.next())
  }

  def first(matcher: T => Boolean) = query(matcher) match {
    case results if (results.isEmpty) => null.asInstanceOf[T]
    case results => results.next()
  }

  def firstOption(matcher: T => Boolean) = query(matcher) match {
    case results if (results.isEmpty) => None
    case results => Some(results.next())
  }
}