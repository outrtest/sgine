package org.sgine.datastore.query

import org.sgine.datastore.Persistable

case class Sort[T <: Persistable, F](field: Field[T, F], direction: SortDirection)
