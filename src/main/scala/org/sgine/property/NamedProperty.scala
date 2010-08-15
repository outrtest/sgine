package org.sgine.property

import org.sgine.property.container.PropertyContainer

trait NamedProperty[T] extends Property[T] {
	protected def _name: String
	
	final def name = {
		val defaultName = "Unnamed"
		_name match {
			case null => {
				this match {
					case lp: ListenableProperty[_] => lp.parent match {
						case pc: PropertyContainer => pc.name(this)
						case _ => defaultName
					}
					case _ => defaultName
				}
			}
			case n => n
		}
	}
	
	abstract override def resolveElement(key: String) = key match {
		case "name" => Some(name)
		case _ => super.resolveElement(key)
	}
}