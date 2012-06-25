package org.sgine.datastore.query

import org.sgine.datastore.Persistable

case class Filter[T <: Persistable, F](field: Field[T, F], operator: Operator, value: F)
