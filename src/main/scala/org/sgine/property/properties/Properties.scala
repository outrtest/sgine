package org.sgine.property.properties

import org.sgine.property.Property

/**
 * A generic trait that defines functions for querying properties by name.
 */
// TODO: Should this extend Updatable?  Well, maybe a group of properties may want to call it internally..
trait Properties {

	/**
	 * The properties currently defined in the class.
	 */
	def properties: List[Property[_]]

	/**
	 * The properties currently defined in the class, as a map from property name to Property.
	 */
	def propertiesByName: Map[Symbol, Property[_]]

	/**
	 * True if this class contains a property with the specified name.
	 */
	def containsProperty(identifier: Symbol): Boolean

	/**
	 * Retrieves the property with the specified identifier from this class.
	 * Returns Some(property) if found, None if no property with the specified name was found.
	 * Cast class cast exception if the property is not of the specified type.
	 */
	def getProperty[T](identifier: Symbol): Option[Property[T]]

}
