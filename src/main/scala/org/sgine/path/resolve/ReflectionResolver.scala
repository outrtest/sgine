package org.sgine.path.resolve

object ReflectionResolver extends Resolver {
	def resolve(root: Any, key: String) = {
		val o = root.asInstanceOf[AnyRef]
		val c = o.getClass
		
		// Try fields first
		try {
			Some(c.getField(key).get(o))
		} catch {
			case nsfe: NoSuchFieldException => {
				// Try methods next
				try {
					Some(c.getMethod(key).invoke(o))
				} catch {
					case nsme: NoSuchMethodException => None
				}
			}
		}
	}
}