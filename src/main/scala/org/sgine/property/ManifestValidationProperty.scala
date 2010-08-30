package org.sgine.property

trait ManifestValidationProperty[T] extends Property[T] {
	abstract override def apply(value: T): Property[T] = {
		if (!manifest.erasure.isInstance(value)) {
			if (manifest.erasure.isPrimitive) {
				// TODO: handle primitives?
			} else {
				val className = value match {
					case null => "null"
					case ref: AnyRef => ref.getClass.getName
				}
				throw new InvalidGenericValueException("Invalid value type on Property! Expected: " + manifest.erasure.getName + ", Received: " + className)
			}
		}
		
		super.apply(value)
	}
}

class InvalidGenericValueException(message: String) extends RuntimeException(message)