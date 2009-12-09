package org.sgine.property.properties

import org.sgine.Updatable
import scala.collection.JavaConversions._
import java.util.concurrent.{ConcurrentHashMap}
import org.sgine.property.Property

/**
 * A trait that provides dynamic properties to a class that extends it.
 * Provides methods to add and remove properties.
 */
// TODO: Add listeners / events for adding and removing properties
trait DynamicProperties extends MutableProperties with Updatable {

  private val _dynamicProperties = new ConcurrentHashMap[Symbol, Property[_]]()

  override def properties: List[Property[_]] = List.fromIterator( _dynamicProperties.values.iterator ) // Create a copy of the current state
	override def propertiesByName: Map[Symbol, Property[_]] = Map() ++ _dynamicProperties.iterator // Create a copy of the current state
	override def containsProperty(identifier: Symbol): Boolean = _dynamicProperties.contains(identifier)
	override def getProperty[T](identifier: Symbol): Option[Property[T]] = {
		if (_dynamicProperties.contains(identifier)) Some(_dynamicProperties.get(identifier).asInstanceOf[Property[T]])
		else None
	}

	override def update(time: Double) = {
		for (p <- _dynamicProperties.values)
			p match {
				case(u: Updatable) => u.update(time)
				case _ =>
    	}
	}

	def addProperty(identifier: Symbol, property: Property[_]) {
    require(identifier != null && !identifier.name.isEmpty, "identifier should not be null or empty")
    require(property != null, "property should not be null")
    require(!_dynamicProperties.contains(identifier), "can not add dynamic property to '"+this+"', a dynamic property with identifier '"+identifier.name+"' already exists.")

    _dynamicProperties.put(identifier, property)
  }

  def removeProperty(identifier: Symbol): Boolean = {
    _dynamicProperties.remove(identifier) != null
  }

}

