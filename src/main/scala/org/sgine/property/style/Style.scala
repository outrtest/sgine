package org.sgine.property.style

import java.lang.reflect.Modifier

import org.sgine.property.Property
import org.sgine.property.container.PropertyContainer

trait Style {
	def condition(stylized: Stylized): Boolean
	
	private var _properties: List[StyleProperty] = Nil
	private var initialized = false
	
	private def initialize() = synchronized {
		if (!initialized) {
			for (m <- getClass.getDeclaredMethods) {
				if (Style.filterOut.contains(m.getName)) {
					// Ignore filtered method names
				} else if (m.getName.startsWith("org$sgine$property$style$Style$")) {
					// Ignore special methods
				} else if (m.getReturnType.toString == "void") {
					// Ignore non-return methods
				} else if (m.getParameterTypes.length > 0) {
					// Ignore methods that take arguments
				} else if (!Modifier.isPublic(m.getModifiers)) {
					// Only use public methods
				} else {
					val sp = if (classOf[Function0[_]].isAssignableFrom(m.getReturnType)) {
						val f = m.invoke(this).asInstanceOf[Function0[_]]
						StyleProperty(m.getName, f().asInstanceOf[AnyRef].getClass, f)
					} else {
						val f = () => m.invoke(this)
						StyleProperty(m.getName, m.getReturnType, f)
					}
					_properties = sp :: _properties
				}
			}
			
			initialized = true
		}
	}
	
	def register(name: String, value: Any) = synchronized {
		_properties = StyleProperty(name, value.asInstanceOf[AnyRef].getClass, () => value) :: _properties
	}
	
	def properties = {
		initialize()
		_properties
	}
	
	def findStylizedProperty(name: String) = properties.find(_.name == name)
	
	def apply(stylized: Stylized) = {
		initialize()
		
		// Cycle through all of the Stylized properties
		for (p <- stylized.properties) p match {
			case sp: StylizedProperty[_] => {
				val name = stylized.name(sp)
				findStylizedProperty(name) match {
					case Some(prop) => changeProperty(prop, sp)		// Apply
					case None => sp.changeStyle(null)				// Remove
				}
			}
			case _ =>
		}
	}
	
	private def changeProperty[T](prop: StyleProperty, sp: StylizedProperty[T]) = {
		if (sp.isAssignable(prop.c)) {
			sp.changeStyle(prop.f.asInstanceOf[Function0[T]])
		} else {
			// Not a type match - remove
			sp.changeStyle(null)
		}
	}
}

case class StyleProperty(name: String, c: Class[_], f: () => Any)

object Style {
	protected val filterOut = List("condition", "properties", "apply")
	
	/**
	 * Removes style details from this Stylized instance.
	 */
	def remove(stylized: Stylized) = {
		for (p <- stylized.properties) p match {
			case sp: StylizedProperty[_] => sp.changeStyle(null)
			case _ =>
		}
	}
}