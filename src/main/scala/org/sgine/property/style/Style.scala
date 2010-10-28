package org.sgine.property.style

import java.lang.reflect.Modifier

import org.sgine.property.Property
import org.sgine.property.container.PropertyContainer

import org.sgine.util.Reflection

trait Style {
	def condition(stylized: Stylized): Boolean
	
	private var _properties: List[StyleProperty] = Nil
	private var _styles: List[Style] = Nil
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
				} else if (classOf[Style].isAssignableFrom(m.getReturnType)) {
					// Manage styles differently
					val style = m.invoke(this).asInstanceOf[Style]
					_styles = style :: _styles
				} else {
					val name = m.getName.replaceAll("_", ".")
					val sp = if (classOf[Function0[_]].isAssignableFrom(m.getReturnType)) {
						val f = m.invoke(this).asInstanceOf[Function0[_]]
						StyleProperty(name, Reflection.boxed(f().asInstanceOf[AnyRef].getClass), f)
					} else {
						val f = () => m.invoke(this)
						StyleProperty(name, Reflection.boxed(m.getReturnType), f)
					}
					_properties = sp :: _properties
				}
			}
			
			initialized = true
		}
	}
	
	def register(name: String, value: Any) = synchronized {
		println("registering: " + name + " - " + value.asInstanceOf[AnyRef].getClass + " - " + value)
		_properties = StyleProperty(name, value.asInstanceOf[AnyRef].getClass, () => value) :: _properties
	}
	
	def update(name: String, value: Any) = register(name, value)
	
	def properties = {
		initialize()
		_properties
	}
	
	def findStylizedProperty(name: String) = properties.find(_.name == name)
	
	def apply(stylized: Stylized): Unit = {
		initialize()
		/*
		// Cycle through all of the Stylized properties
		for (p <- stylized.properties) p match {
			case sp: StylizedProperty[_] => {
				val name = stylized.name(sp)
				findStylizedProperty(name) match {
					case Some(prop) => {
						changeProperty(prop, sp)		// Apply
					}
					case None => sp.changeStyle(null)				// Remove
				}
			}
			case s: Stylized => {	// Apply child styles to stylized children
				_styles.find(_.condition(s)) match {
					case Some(style) => style(s) // Apply sub-style
					case None => // Ignore
				}
			}
			case _ =>
		}*/
		
		// Revert all styles to null first
		for (p <- stylized.properties) p match {
			case sp: StylizedProperty[_] => sp.changeStyle(null)
			case _ =>
		}
		
		// Apply styles
		for (sp <- properties) {
			org.sgine.path.OPath.resolve(stylized, sp.name) match {
				case Some(result) => result match {
					case stylizedProperty: StylizedProperty[_] => changeProperty(sp, stylizedProperty)
					case _ => //println("NOT STYLIZED: " + result.asInstanceOf[AnyRef].getClass + " - " + sp.name)
				}
				case None => // Unable to resolve
			}
		}
		
		// Process child styles
		for (childStyle <- _styles) {
			findStylized(childStyle, stylized.properties.toList) match {
				case Some(stylized) => childStyle(stylized)
				case None => // Ignore
			}
		}
	}
	
	private def findStylized(style: Style, list: List[Property[_]]): Option[Stylized] = {
		if (list != Nil) {
			list.head match {
				case stylized: Stylized if (style.condition(stylized)) => Some(stylized)
				case _ => findStylized(style, list.tail)
			}
		} else {
			None
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