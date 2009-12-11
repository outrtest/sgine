package org.sgine.property.container

import org.sgine._
import org.sgine.event._
import org.sgine.property._

import scala.collection.JavaConversions._

import java.util.concurrent._

trait PropertyContainer extends Iterable[Property[_]] with Updatable with Listenable {
	def parent: Listenable = null
	private lazy val properties = initializeDeclaredProperties()
	protected val aliases = new ConcurrentHashMap[Symbol, Property[_]]()
	
	def apply(name: Symbol) = aliases.get(name)
	
	def contains(name: Symbol) = aliases.containsKey(name)
	
	def update(time: Double) = {
		for (p <- this) p match {
			case u: Updatable => u.update(time)
			case _ =>
		}
	}
	
	def iterator(): Iterator[Property[_]] = properties.iterator()
	
	protected def +=(p: Property[_]): PropertyContainer = {
		properties.add(p)
		p match {
			case np: NamedProperty => aliases.put(np.name, p)
			case _ =>
		}
		
		this
	}
	
	protected def -=(p: Property[_]): PropertyContainer = {
		properties.remove(p)
		p match {
			case np: NamedProperty => aliases.remove(np.name)
			case _ =>
		}
		
		this
	}
	
	private def initializeDeclaredProperties() = {
		var props = new CopyOnWriteArraySet[Property[_]]()
		
		for (f <- getClass.getDeclaredFields) {
			f.setAccessible(true)
			
			f.get(this) match {
				case p: Property[_] => {
					props.add(p)
					println("*** Setting alias: " + f.getName)
					aliases.put(Symbol(f.getName), p)
				}
				case _ =>
			}
		}
		
		props
	}
}

object PropertyContainer {
	def apply(parent: Listenable) = new PropertyContainerImplementation(parent)
}

class PropertyContainerImplementation(override val parent: Listenable) extends PropertyContainer