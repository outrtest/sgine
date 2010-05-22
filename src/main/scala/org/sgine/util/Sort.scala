package org.sgine.util

import scala.collection.mutable.ResizableArray

object Sort {
	@scala.annotation.tailrec
	def bubbleSort[A](array: ResizableArray[A], sortFunction: (A, A) => Boolean): ResizableArray[A] = {
		var swapped = false
		for (i <- array.indices.init) {		// Process all but the last element
			if (sortFunction(array(i + 1), array(i))) {
				val t = array(i)
				array(i) = array(i + 1)
				array(i + 1) = t
				swapped = true
			}
		}
		
		if (swapped) {
			bubbleSort(array, sortFunction)
		} else {
			array
		}
	}
}