package org.sgine.property.style

/**
 * SelectorStyle provides a CSS-like method of selecting Stylized instances
 * to apply to. See Selector for further details on the types of selectors
 * that may be provided.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class SelectorStyle(groups: SelectorGroup*) extends Style {
	def this(selectorStyle: String) = {
		this(SelectorStyle.createGroups(selectorStyle): _*)
	}
	
	def condition(stylized: Stylized) = groups.find(_.matches(stylized)) != None
}

object SelectorStyle {
	private var map = Map.empty[String, Class[_]]
	
	def registerClass(alias: String, c: Class[_]) = synchronized {
		map += alias -> c
	}
	
	def lookupClass(alias: String) = map.get(alias)
	
	def apply(selectorStyle: String) = {
		new SelectorStyle(createGroups(selectorStyle): _*)
	}
	
	def createGroups(selectorStyle: String) = {
		var groups: List[SelectorGroup] = Nil
		var selectors: List[Selector] = Nil
		
		val b = new StringBuilder()
		for (c <- selectorStyle) {
			if (b.length > 0) {
				if ((c == '#') || (c == '.') || (c == ' ') || (c == ',')) {
					selectors = createSelector(b.toString.trim) :: selectors
					b.clear()
					
					if (c == ',') {
						groups = SelectorGroup(selectors: _*) :: groups
						selectors = Nil
					}
				}
			}
			if (c != ' ') {
				b.append(c)
			}
		}
		if (b.length > 0) {
			selectors = createSelector(b.toString.trim) :: selectors
		}
		if (selectors.length > 0) {
			groups = SelectorGroup(selectors: _*) :: groups
		}
		
		groups
	}
	
	def createSelector(selector: String) = {
		if (selector.startsWith("#")) {
			IdSelector(selector.substring(1))
		} else if (selector.startsWith(".")) {
			StyleNameSelector(selector.substring(1))
		} else {
			ClassSelector(selector)
		}
	}
}

case class SelectorGroup(seq: Selector*) {
	def matches(stylized: Stylized) = {
		recursiveMatch(stylized, seq.toList)
	}
	
	private def recursiveMatch(stylized: Stylized, list: List[Selector]): Boolean = {
		val s = list.head
		if (s.matches(stylized)) {
			if (list.tail != Nil) {
				stylized.parent match {
					case parent: Stylized => recursiveMatch(parent, list.tail)
					case _ => false
				}
			} else {		// Finished selectors
				true
			}
		} else {
			false
		}
	}
}

trait Selector {
	def matches(stylized: Stylized): Boolean
}

case class IdSelector(id: String) extends Selector {
	def matches(stylized: Stylized) = {
		stylized.id() == id
	}
}

case class ClassSelector(_className: String) extends Selector {
	val className = SelectorStyle.lookupClass(_className) match {
		case Some(c) => c.getSimpleName
		case None => _className
	}
	
	def matches(stylized: Stylized) = {
		isClassMatch(stylized.getClass)
	}
	
	private def isClassMatch(c: Class[_]): Boolean = {
		if (c.getSimpleName == className) {
			true
		} else if ((c.getSimpleName.endsWith("$")) && (c.getSimpleName.substring(0, c.getSimpleName.length - 1) == className)) {
			true		// Handle companion class naming
		} else {
			c.getSuperclass match {
				case null => {
					println("Unable to match: " + c.getSimpleName + " - " + className)
					false
				}
				case sc => isClassMatch(sc)
			}
		}
	}
}

case class StyleNameSelector(styleName: String) extends Selector {
	def matches(stylized: Stylized) = {
		stylized.style() == styleName
	}
}