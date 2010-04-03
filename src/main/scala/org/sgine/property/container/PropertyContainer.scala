package org.sgine.property.container

import scala.collection.immutable.HashMap
import org.sgine._
import org.sgine.event._
import org.sgine.property._

import scala.collection.JavaConversions._

trait PropertyContainer extends Iterable[Property[_]] with Listenable {
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
	
	def apply(map: Map[String, Any], autoCommit: Boolean): Unit = {
		for ((key, value) <- map) {
			apply(key, value, autoCommit)
		}
	}
	
	def apply[T](key: String, value: T, autoCommit: Boolean): Unit = {
		apply(key) match {
			case s: Some[Property[T]] => {
				val p = s.get
				p := value
				if (autoCommit) {
					p match {
						case tp: TransactionalProperty[T] => tp.commit
					}
				}
			}
			case None =>
		}
	}
	
	def commit() = {
		var changed = false
		for (p <- this) p match {
			case tp: TransactionalProperty[_] => {
				if (tp.commit()) changed = true
			}
			case _ =>
		}
		
		changed
	}
	
	def revert() = {
		var changed = false
		for (p <- this) p match {
			case tp: TransactionalProperty[_] => {
				if (tp.revert()) changed = true
			}
			case _ =>
		}
		
		changed
	}
	
	def uncommitted = {
		var u = false
		for (p <- this) p match {
			case tp: TransactionalProperty[_] => {
				if (tp.uncommitted) u = true
			}
			case _ =>
		}
		u
	}
	
	def contains(name: String) = {
		initialize()
		aliases.contains(name)
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
					
					properties = properties.reverse
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