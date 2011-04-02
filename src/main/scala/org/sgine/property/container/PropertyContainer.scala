package org.sgine.property.container

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import scala.reflect.ClassManifest

import org.sgine._
import org.sgine.event._
import org.sgine.property._

import org.sgine.work.DefaultWorkManager
import org.sgine.work.Updatable

trait PropertyContainer extends Listenable with Property[Int] {
	val manifest = ClassManifest.Int

	private val _properties = new scala.collection.mutable.ArrayBuffer[Property[_]]()
	private var aliases = Map.empty[String, Property[_]]

	private var initialized = false
	private val initializeStart = System.currentTimeMillis

  private def reflected = reflectProperties(getClass, Nil)
	
	PropertyContainer.initialize(this)
	
	def properties = {
		initialize()
		_properties
	}
	
	def apply() = -1
	
	def apply(value: Int): Property[Int] = this
	
	def property(name: String): Option[Property[_]] = {
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
		property(key) match {
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
	
	def addProperty(p: Property[_]): PropertyContainer = {
		if (!_properties.contains(p)) {										// No duplicates allowed
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

    aliases.find(t => t._2 == p).map(_._1).getOrElse(null)
	}
	
	private def reflectProperties(c: Class[_], list: List[Field]): List[Field] = {
		var l = list
		
		for (f <- c.getDeclaredFields) {
			if ((classOf[Property[_]].isAssignableFrom(f.getType)) && (f.getName.indexOf("$") == -1)) {
				f.setAccessible(true)
				l = f :: l
			}
		}
		
		if (c.getSuperclass != null) {
			reflectProperties(c.getSuperclass, l)
		} else {
			l
		}
	}
	
	final private def initialize() = {
		if (!initialized) {
			synchronized {
				var properties: List[Property[_]] = Nil
				var finished = true
				
				try {
					for (f <- reflected) {
						val property = f.get(this).asInstanceOf[Property[_]]
						if (property != null) {
							properties = property :: properties
							if (!aliases.contains(f.getName)) {
								aliases += f.getName -> property
							}
						} else {
							finished = false
						}
					}
				} catch {
					case exc: java.lang.reflect.InvocationTargetException => // Ignore - not initialized
				}
				
				if (finished) {
					for (p <- properties) {
						addProperty(p)
					}
					_properties.reverse
					initialized = true
					initializedProperties()
				}
			}
		}
	}
	
	protected def initializedProperties() = {
	}
	
	abstract override def resolveElement(key: String) = {
		if (contains(key)) {
			property(key)
		} else {
			super.resolveElement(key)
		}
	}
	
	override def toString() = "PropertyContainer(" + getClass.getSimpleName + ")"
}

object PropertyContainer extends Updatable {
	private var uninitted: List[PropertyContainer] = Nil
	
	protected def initialize(pc: PropertyContainer) = synchronized {
		uninitted = pc :: uninitted
	}
	
	def apply(parent: Listenable) = new PropertyContainerImplementation(parent)
	
	override def update(time: Double) = {
		super.update(time)
		
		if (uninitted.length > 0) {
			val time = System.currentTimeMillis
			for (pc <- uninitted) {
				pc.initialize()
				
				if ((pc.initialized) || (time - pc.initializeStart > 5000)) {
					synchronized {
						uninitted = uninitted.filterNot(_ == pc)
					}
				}
			}
		}
	}
	
	initUpdatable()
}

class PropertyContainerImplementation(override val parent: Listenable) extends PropertyContainer