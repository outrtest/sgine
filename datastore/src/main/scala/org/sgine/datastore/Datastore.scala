package org.sgine.datastore

/**
 * Datastore implements functionality for working with a backing datastore.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Datastore {
  def transaction(f: => Unit) = try {
    f
    commit()
  } catch {
    case throwable => {
      rollback()
      throw throwable
    }
  }

  def persist(obj: AnyRef): Unit

  def persistAll(objects: AnyRef*) = objects.foreach(persist)

  def delete(obj: AnyRef): Unit

  def commit(): Unit

  def rollback(): Unit

  def byExample[T](obj: T)(implicit manifest: Manifest[T]): Seq[T]

  def all[T]()(implicit manifest: Manifest[T]): Seq[T]

  def query[T](matcher: T => Boolean)(implicit manifest: Manifest[T]): Seq[T]

  def close(): Unit

  def firstByExample[T](obj: T)(implicit manifest: Manifest[T]) = byExample[T](obj)(manifest) match {
    case results if (results.isEmpty) => null.asInstanceOf[T]
    case results => results.head
  }

  def firstOptionByExample[T](obj: T)(implicit manifest: Manifest[T]) = firstByExample[T](obj)(manifest) match {
    case null => None
    case result => Some(result)
  }

  def first[T]()(implicit manifest: Manifest[T]) = all[T]()(manifest) match {
    case results if (results.isEmpty) => null.asInstanceOf[T]
    case results => results.head
  }

  def firstOption[T]()(implicit manifest: Manifest[T]) = all[T]()(manifest) match {
    case results if (results.isEmpty) => None
    case results => Some(results.head)
  }

  def first[T](matcher: T => Boolean)(implicit manifest: Manifest[T]) = query(matcher)(manifest) match {
    case results if (results.isEmpty) => null.asInstanceOf[T]
    case results => results.head
  }

  def firstOption[T](matcher: T => Boolean)(implicit manifest: Manifest[T]) = query(matcher)(manifest) match {
    case results if (results.isEmpty) => None
    case results => Some(results.head)
  }
}