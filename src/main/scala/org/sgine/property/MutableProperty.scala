package org.sgine.property;

/**
 * MutableProperty provides an extremely simple Property implementation
 * that may be modified. Calls to <code>apply(t:T)</code> will modify the
 * current instance and return a reference back to itself.
 * 
 * @author Matt Hicks
 */
class MutableProperty[T] extends Property[T] {
	def this(initialValue: T) = {
		this()
		apply(initialValue)
	}

	@volatile protected var value: T = _

	def apply(): T = {
		value;
	}
	
	def apply(value: T): Property[T] = {
		// this is a hack
		import simplex3d.math.doublem._
		if (this.value.isInstanceOf[Mat3x4d]) {
			this.value.asInstanceOf[Mat3x4d] := value.asInstanceOf[Mat3x4d]
			if (this.isInstanceOf[ChangeableProperty[_]]) {
				this.asInstanceOf[ChangeableProperty[T]].changed(this.value, this.value)
			}
		}
		else
			this.value = value;
		
		this;
	}
}