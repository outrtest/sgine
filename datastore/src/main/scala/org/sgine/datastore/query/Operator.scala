package org.sgine.datastore.query

import org.sgine.{EnumEntry, Enumerated}

sealed class Operator extends EnumEntry[Operator]

object Operator extends Enumerated[Operator] {
  val < = new Operator
  val <= = new Operator
  val > = new Operator
  val >= = new Operator
  val equal = new Operator
  val nequal = new Operator
}
