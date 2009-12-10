package org.sgine.property.properties

import org.sgine.property.Property

/**
 * A trait that provides both static property discovery as well as ability to add and remove properties dynamically.
 */
trait StaticAndDynamicProperties extends StaticProperties with MutableProperties {
	private val dynamic: DynamicProperties = new DynamicProperties() {}

	override def update(time: Double) = {
		super.update(time)
		dynamic.update(time)
	}

	override def getProperty[T](identifier: Symbol) = super.getProperty[T](identifier).orElse(dynamic.getProperty[T](identifier))
	override def containsProperty(identifier: Symbol) = super.containsProperty(identifier) || dynamic.containsProperty(identifier)
	override def propertiesByName = super.propertiesByName ++ dynamic.propertiesByName
	override def properties = super.properties ::: dynamic.properties

	def addProperty(identifier: Symbol, property: Property[_]) {
		require(!super.containsProperty(identifier), "can not add dynamic property to '"+this+"', a static property with identifier '"+identifier.name+"' already exists.")
		require(!dynamic.containsProperty(identifier), "can not add dynamic property to '"+this+"', a dynamic property with identifier '"+identifier.name+"' already exists.")
		
		dynamic.addProperty(identifier, property)
	}
	
	def removeProperty(identifier: Symbol): Boolean = dynamic.removeProperty(identifier)

	/**
	 * The dynamic properties currently defined in this class.
	 */
	def dynamicPropertiesByName: Map[Symbol, Property[_]] = dynamic.propertiesByName
}