package com.sgine.util

import java.util.concurrent._;

object JavaConversions {
	implicit def clq2iterable[T](clq:ConcurrentLinkedQueue[T]) =
		new Iterable[T]() {
			override def iterator:Iterator[T] = {
				val i = clq.iterator();
				new Iterator[T]() {
					def hasNext() = {
						i.hasNext();
					}
					
					def next() = {
						i.next();
					}
				}
			}
		}
}
