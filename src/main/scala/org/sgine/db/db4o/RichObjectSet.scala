package org.sgine.db.db4o

import com.db4o.ObjectSet

class RichObjectSet[T](objectSet: ObjectSet[T]) extends Iterator[T] {
	def hasNext = objectSet.hasNext
	def next = objectSet.next
}