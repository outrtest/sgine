package org.sgine.property.container

import org.sgine._
import org.sgine.event._
import org.sgine.property._

trait PropertyContainer extends Iterable[Property[_]] with Updatable with Listenable {
	def apply(name: Symbol): Property[_] = {
		for (p <- this) p match {
			case np: NamedProperty => {
				if (np.name == name) {
					return p
				}
			}
			case _ =>
		}
		null
	}
	
	def contains(name: Symbol): Boolean = {
		apply(name) != null
	}
	
	def update(time: Double) = {
		for (p <- this) p match {
			case u: Updatable => u.update(time)
			case _ =>
		}
	}
}