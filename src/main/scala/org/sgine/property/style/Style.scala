package org.sgine.property.style

import java.lang.reflect.Modifier

import org.sgine.property.Property
import org.sgine.property.container.PropertyContainer

trait Style {
	def condition: (Stylized) => Boolean
	
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
	
	def properties = {
		initialize()
		_properties
	}
	
	def apply(stylized: Stylized) = {
		initialize()
		
		for (prop <- properties) {
			stylized(prop.name) match {
				case Some(p) => p match {
					case sp: StylizedProperty[_] => changeProperty(prop, sp)
					case _ => // Not a stylized property - ignore
				}
				case None => // No property by this name - ignore
			}
		}
	}
	
	private def changeProperty[T](prop: StyleProperty, sp: StylizedProperty[T]) = {
		if (sp.isAssignable(prop.c)) {
			sp.changeStyle(prop.f.asInstanceOf[Function0[T]])
		} else {
			// Not a type match - ignore
		}
	}
}

case class StyleProperty(name: String, c: Class[_], f: () => Any)

object Style {
	protected val filterOut = List("condition", "properties", "apply")
	
	def named(name: String)(stylized: Stylized) = name == stylized.id()
	
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