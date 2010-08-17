package org.sgine.property;

import scala.reflect.Manifest

/**
 * ImmutableProperty provides an extremely simple Property implementation
 * that may not be changed after creation. Calls to <code>apply(t:T)</code>
 * will return a new instance of the ImmutableProperty containing the new
 * value. 
 * 
 * @author Matt Hicks
 */
class ImmutableProperty[T](value:T)(protected implicit val manifest: Manifest[T]) extends Property[T] {
	def apply():T = {
		value;
	}
	
	def apply(value:T):Property[T] = {
		new ImmutableProperty[T](value);
	}
}