package org.sgine.property.container

import scala.collection.immutable.HashMap
import org.sgine._
import org.sgine.event._
import org.sgine.property._

trait PropertyContainer extends Listenable with Property[Int] {
	private var _properties: List[Property[_]] = Nil
	private var aliases = new HashMap[String, Property[_]]()
	private val staticPropertyFields = getStaticPropertyFields
	
	private var initialized = false
	
	def apply() = -1
	
	def apply(value: Int) = throw new UnsupportedOperationException("Not supported!")
	
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
		for (p <- properties) p match {
			case tp: TransactionalProperty[_] => {
				if (tp.commit()) changed = true
			}
			case _ =>
		}
		
		changed
	}
	
	def revert() = {
		var changed = false
		for (p <- properties) p match {
			case tp: TransactionalProperty[_] => {
				if (tp.revert()) changed = true
			}
			case _ =>
		}
		
		changed
	}
	
	def uncommitted = {
		var u = false
		for (p <- properties) p match {
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
	
	def properties = _properties
	
	def addProperty(p: Property[_]): PropertyContainer = {
		if (!_properties.contains(p)) {			// No duplicates allowed
			_properties = p :: _properties									// Add the property to the list

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
	
	def removeProperty(p: Property[_]): PropertyContainer = {
		_properties -= p
		p match {
			case np: NamedProperty => aliases -= np.name
			case _ =>
		}
		
		this
	}
	
	def name(p: Property[_]) = getStaticPropertyName(p)
	
	private def getStaticPropertyName(p: Property[_]): String = {
		initialize()
		
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
				for (f <- staticPropertyFields) {
					val p = f.get(this).asInstanceOf[Property[_]]
					addProperty(p)
					if (!aliases.contains(f.getName)) {
						aliases += f.getName -> p
					}
				}
				
				_properties = _properties.reverse
				initialized = true
			}
		}
	}
}

object PropertyContainer {
	def apply(parent: Listenable) = new PropertyContainerImplementation(parent)
}

class PropertyContainerImplementation(override val parent: Listenable) extends PropertyContainer