package org.sgine.property.style

import java.lang.reflect.Modifier

import org.sgine.log._

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
						val wrap = !f.isInstanceOf[Property[_]]
						StyleProperty(name, Reflection.boxed(f().asInstanceOf[AnyRef].getClass), f, wrap)
					} else {
						val f = () => m.invoke(this)
						StyleProperty(name, Reflection.boxed(m.getReturnType), f, false)
					}
					_properties = sp :: _properties
				}
			}
			
			initialized = true
		}
	}
	
	def register(name: String, value: Any) = synchronized {
		_properties = StyleProperty(name, value.asInstanceOf[AnyRef].getClass, () => value, false) :: _properties
	}
	
	def update(name: String, value: Any) = register(name, value)
	
	def properties = {
		initialize()
		_properties
	}
	
	def findStylizedProperty(name: String) = properties.find(_.name == name)
	
	def apply(stylized: Stylized): Unit = {
		initialize()
		
		var stylizedProperties = stylized.properties.toList
		
		// Apply styles
		var states = false
		for (sp <- properties) {
//			println("Applying: " + sp.name + " to " + stylized)
			org.sgine.path.OPath.resolve(stylized, sp.name) match {
				case Some(result) => result match {
					case stylizedProperty: StylizedProperty[_] => {
						changeProperty(sp, stylizedProperty)
						stylizedProperties = stylizedProperties.filterNot(_ == stylizedProperty)
					}
					case state: org.sgine.property.state.State => {
						states = true
						sp.f() match {
							case (path: String, value: Any) => state(path) = value
							case _ => throw new RuntimeException("State-based stylization must reference Tuple2[String, Any].")
						}
					}
					case _ => warn("NOT STYLIZED: " + result.asInstanceOf[AnyRef].getClass + " - " + sp.name)
				}
				case None => //println("Cannot resolve: " + sp.name) // Unable to resolve
			}
		}
		
		// Revert all styles to null that we didn't match
		for (p <- stylizedProperties) p match {
			case sp: StylizedProperty[_] => sp.changeStyle(null)
			case _ =>
		}
		
		// Process child styles
		for (childStyle <- _styles) {
			findStylized(childStyle, stylized.properties.toList) match {
				case Some(stylized) => childStyle(stylized)
				case None => // Ignore
			}
		}
		
		if (states) {
			stylized match {
				case stateful: org.sgine.property.state.Stateful => stateful.updateState()
				case _ =>
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
			val f = prop.f.asInstanceOf[Function0[T]]
			val wrapped = prop.wrap match {
				case true => {
					val value = f()
					() => value
				}
				case false => f
			}
			sp.changeStyle(wrapped)
		} else {
			// Not a type match - remove
			sp.changeStyle(null)
		}
	}
}

case class StyleProperty(name: String, c: Class[_], f: () => Any, wrap: Boolean)

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