package org.sgine.property.container

import scala.collection.immutable.HashMap
import org.sgine._
import org.sgine.event._
import org.sgine.property._

import scala.collection.JavaConversions._

trait PropertyContainer extends Iterable[Property[_]] with Updatable with Listenable {
	def parent: Listenable = null
	private var properties: List[Property[_]] = Nil
	private var aliases = new HashMap[String, Property[_]]()
	private val staticPropertyFields = getStaticPropertyFields
	
	private var initialized = false
	
	def apply(name: String): Option[Property[_]] = {
		initialize()
		
		for ((n, p) <- aliases) {
			if (name.equalsIgnoreCase(n)) {
				return new Some(p)
			}
		}
		None
	}
	
	def contains(name: String) = {
		initialize()
		aliases.contains(name)
	}
	
	def update(time: Double) = {
		for (p <- this) p match {
			case u: Updatable => u.update(time)
			case _ =>
		}
	}
	
	def iterator(): Iterator[Property[_]] = {
		initialize()
		properties.iterator
	}
	
	def +=(p: Property[_]): PropertyContainer = {
		if (!properties.contains(p)) {			// No duplicates allowed
			properties = p :: properties									// Add the property to the list

			p match {														// Assign an alias if one already exists
				case np: NamedProperty => {
					if (np.name != null) {
						aliases += np.name -> p
					}
				}
				case _ =>
			}
		}
		
		this
	}
	
	def -=(p: Property[_]): PropertyContainer = {
		properties.remove(p)
		p match {
			case np: NamedProperty => aliases.remove(np.name)
			case _ =>
		}
		
		this
	}
	
	def name(p: Property[_]) = getStaticPropertyName(p)
	
	private def getStaticPropertyName(p: Property[_]): String = {
		for (f <- staticPropertyFields) {
			if (f.get(this) == p) {
				return f.getName
			}
		}
		return null
	}
	
	private def getStaticPropertyFields = {
		var props: List[java.lang.reflect.Field] = Nil
		for (f <- getClass.getDeclaredFields) {
			if (classOf[Property[_]].isAssignableFrom(f.getType)) {
				f.setAccessible(true)
				props = f :: props
			}
		}
		
		props
	}
	
	private def initialize() = {
		if (!initialized) {
			synchronized {
				if (!initialized) {
					for (f <- staticPropertyFields) {
						val p = f.get(this).asInstanceOf[Property[_]]
						this += p
						if (!aliases.contains(f.getName)) {
							aliases += f.getName -> p
						}
					}
					
					initialized = true
				}
			}
		}
	}
}

object PropertyContainer {
	def apply(parent: Listenable) = new PropertyContainerImplementation(parent)
}

class PropertyContainerImplementation(override val parent: Listenable) extends PropertyContainer