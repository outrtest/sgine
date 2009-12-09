package org.sgine.property.properties

import org.sgine.property.Property

/**
 * Allows querying properties by name, as well as adding and removing named properties.
 */
trait MutableProperties extends Properties {

	/**
	 * Adds the specified property to this class.
	 *
	 * Throws exception if the identifier is null or empty, or the property is null,
	 * or the identifier is already used by a property.
	 */
	def addProperty(identifier: Symbol, property: Property[_])

  /**
   * Removes the property with the specified identifier.
   * Returns true on success and false if no such property was found or it could not be removed.
   */
  def removeProperty(identifier: Symbol): Boolean

}

