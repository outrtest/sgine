package org.sgine

package object datastore extends Datastore {
  var datastore: Datastore = _

  def persist(obj: AnyRef) = datastore.persist(obj)

  def delete(obj: AnyRef) = datastore.delete(obj)

  def commit() = datastore.commit()

  def rollback() = datastore.rollback()

  def byExample[T](obj: T)(implicit manifest: Manifest[T]) = datastore.byExample(obj)(manifest)

  def all[T]()(implicit manifest: Manifest[T]) = datastore.all()(manifest)

  def query[T](matcher: (T) => Boolean)(implicit manifest: Manifest[T]) = datastore.query(matcher)(manifest)

  def close() = datastore.close()
}