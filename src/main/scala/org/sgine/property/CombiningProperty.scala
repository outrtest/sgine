package org.sgine.property

/**
 * CombiningProperty provides the ability to combine
 * another property to this based on a combining
 * function.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait CombiningProperty[T] extends Property[T] {
	/**
	 * Property to combine with this property in order
	 * to attain the resulting value.
	 * 
	 * @return
	 * 		Property[T]
	 */
	protected def combine: Property[T]
	/**
	 * Function responsible for combining the value
	 * from <code>combine</code> to the current value
	 * for this property to resolve the final value.
	 * 
	 * The function is invoked: (combine(), value): T
	 * 
	 * @return
	 * 		combined value
	 */
	protected def combineFunction: (T, T) => T
	
	abstract override def apply() = {
		val value = super.apply()
		
		// TODO: validate the value might be allowed to equal null?
		if ((combine != null) && (combineFunction != null) && (value != null)) {
			combineFunction(combine(), value)
		} else {
			value
		}
	}
}