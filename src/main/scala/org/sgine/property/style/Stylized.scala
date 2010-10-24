package org.sgine.property.style

import java.lang.ref.WeakReference

import org.sgine.property.AdvancedProperty
import org.sgine.property.container.PropertyContainer

/**
 * Stylized trait should be mixed into any class to
 * provide stylization support. Stylized instances are
 * automatically managed in the Stylized companion class
 * as WeakReferences.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Stylized extends PropertyContainer {
	/**
	 * Unique identification name of this object.
	 */
	val id = new AdvancedProperty[String](null, this)
	
	/**
	 * Style name associated to this object. This
	 * allows grouping of objects by style name.
	 */
	val style = new AdvancedProperty[String](null, this)
	
	/**
	 * Updates the stylization on this instance only.
	 * This will utilize the currently active Theme or
	 * remove all stylization if there is no currently
	 * active theme.
	 */
	def updateStyle() = {
		Theme.current() match {
			case null => Style.remove(this)
			case theme => theme.validate(this)
		}
	}
	
	override protected def initializedProperties() = {
		// Wait until the PropertyContainer has properly registered its children
		Stylized.register(this)
	}
}

object Stylized {
	private var list: List[WeakReference[Stylized]] = Nil
	
	protected def register(s: Stylized) = {
		synchronized {
			if (!contains(s)) {		// Make sure it only gets added once
				list = new WeakReference(s) :: list
				
				// Update the style
				s.updateStyle()
			}
		}
	}
	
	protected def contains(stylized: Stylized) = list.find(_.get eq stylized) != None
	
	/**
	 * Processes through every Stylized instance applying
	 * the proper Style based on the passed Theme. If the
	 * passed theme is null all styles are removed.
	 * 
	 * @param theme
	 */
	def apply(theme: Theme) = {
		for (stylized <- list) {
			stylized.get match {
				case null => synchronized {
					list = list.filterNot(_ == stylized)
				}
				case s => if (theme != null) {
					// Validate Stylized against current theme.
					theme.validate(s)
				} else {
					// If null is passed for the theme, remove styles
					Style.remove(s)
				}
			}
		}
	}
}