package org.sgine.datastore

import java.util
import org.sgine.{Enumerated, EnumEntry}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class DatastoreQuery[T <: Persistable](collection: DatastoreCollection[T],
                                            _skip: Int = 0,
                                            _limit: Int = Int.MaxValue,
                                            _filters: List[Filter[T, _]] = List.empty[Filter[T, _]])
                                            (implicit manifest: Manifest[T]) extends Iterable[T] {
  def skip(s: Int) = copy(_skip = s)
  def limit(l: Int) = copy(_limit = l)
  def filter(filter: Filter[T, _]) = copy(_filters = filter :: _filters)

  // TODO: implement sorting

  def iterator = collection.executeQuery(this)
}

case class Filter[T <: Persistable, F](field: Field[T, F], operator: Operator, value: F)

case class Field[T <: Persistable, F](name: String) {
  def <(value: F) = Filter(this, Operator.<, value)
  def <=(value: F) = Filter(this, Operator.<=, value)
  def >(value: F) = Filter(this, Operator.>, value)
  def >=(value: F) = Filter(this, Operator.>=, value)

  def equal(value: F) = Filter(this, Operator.equal, value)
  def nequal(value: F) = Filter(this, Operator.nequal, value)
}

object Field {
  def id[T <: Persistable] = Field[T, util.UUID]("_id")
}

sealed class Operator extends EnumEntry[Operator]

object Operator extends Enumerated[Operator] {
  val < = new Operator
  val <= = new Operator
  val > = new Operator
  val >= = new Operator
  val equal = new Operator
  val nequal = new Operator
}