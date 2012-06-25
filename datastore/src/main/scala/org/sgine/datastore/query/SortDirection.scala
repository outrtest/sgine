package org.sgine.datastore.query

import org.sgine.{EnumEntry, Enumerated}

sealed class SortDirection extends EnumEntry[SortDirection]

object SortDirection extends Enumerated[SortDirection] {
  val Ascending = new SortDirection
  val Descending = new SortDirection
}
