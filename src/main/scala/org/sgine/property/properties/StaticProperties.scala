package org.sgine.property.properties

import org.sgine._
import java.lang.reflect.Field;
import org.sgine.property._;

/**
 * Retrieves properties defined in (val) fields in a class and exposes them through the Properties trait.
 */
trait StaticProperties extends Properties with Updatable {
	private lazy val _propertiesByName: Map[Symbol, Property[_]] = fetchDeclaredProperties()
	private lazy val _properties: List[Property[_]] = _propertiesByName.values.toList

	def properties: List[Property[_]] = _properties
	def propertiesByName: Map[Symbol, Property[_]] = _propertiesByName
	def containsProperty(identifier: Symbol): Boolean = _propertiesByName.contains(identifier)
	def getProperty[T](identifier: Symbol): Option[Property[T]] = _propertiesByName.get(identifier).asInstanceOf[Option[Property[T]]]
	def update(time:Double) = {
		for (p <- _properties) 
			p match {
				case(u: Updatable) => u.update(time)
				case _ =>
    	}
	}

	private def fetchDeclaredProperties(): Map[Symbol, Property[_]] = {
		var props: Map[Symbol, Property[_]] = Map()
		
		for (f: Field <- getClass.getDeclaredFields) {
			// Allow accessing properties even if they are private.
			// TODO: Is this a good idea?  I assume it is needed for updating them?  Should we return the access level to the earlier if the field wasn't a property?
			f.setAccessible(true)

			f.get(this) match {
				case p:Property[_] => {
					// Wrap name in Symbol as it caches instances with the same name,
					// and allows easier use from client code ('foo instead of "foo").
					val name = Symbol(f.getName)

					props += (name -> p)
				}
				case _ =>
			}
		}

		props
	}
}