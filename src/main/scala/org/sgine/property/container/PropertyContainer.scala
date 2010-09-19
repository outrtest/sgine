package org.sgine.property.container

import scala.collection.immutable.HashMap
import scala.reflect.ClassManifest

import org.sgine._
import org.sgine.event._
import org.sgine.property._

trait PropertyContainer extends Listenable with Property[Int] {
	protected val manifest = ClassManifest.Int
	
	private val _properties = new scala.collection.mutable.ArrayBuffer[Property[_]]()
	private var aliases = new HashMap[String, Property[_]]()
	private val staticPropertyFields = getStaticPropertyFields
	
	private var initialized = false
	
	def apply() = -1
	
	def apply(value: Int): Property[Int] = this
	
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
			case Some(p) => {
				val pt = p.asInstanceOf[Property[T]]
				pt := value
				if (autoCommit) {
					p match {
						case tp: TransactionalProperty[_] => tp.commit
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
	
	def properties = {
		initialize()
		_properties
	}
	
	def addProperty(p: Property[_]): PropertyContainer = {
		if (!_properties.contains(p)) {			// No duplicates allowed
			_properties += p												// Add the property to the list

			p match {														// Assign an alias if one already exists
				case np: NamedProperty[_] => {
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
			case np: NamedProperty[_] => aliases -= np.name
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
				var properties: List[Property[_]] = Nil
				var uninitialized = false
				for (f <- staticPropertyFields) {
					val p = f.get(this).asInstanceOf[Property[_]]
					if (p == null) {
						uninitialized = true
					} else {
						properties = p :: properties
						if (!aliases.contains(f.getName)) {
							aliases += f.getName -> p
						}
					}
				}
				
				if (!uninitialized) {
					for (p <- properties) {
						addProperty(p)
					}
					_properties.reverse
					initialized = true
				}
			}
		}
	}
	
	abstract override def resolveElement(key: String) = {
		if (contains(key)) {
			this(key)
		} else {
			super.resolveElement(key)
		}
	}
}

object PropertyContainer {
	def apply(parent: Listenable) = new PropertyContainerImplementation(parent)
}

class PropertyContainerImplementation(override val parent: Listenable) extends PropertyContainer