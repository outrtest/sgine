package org.sgine.property.style

import org.sgine.core.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.container.PropertyContainer
import org.sgine.property.event.PropertyChangeEvent

/**
 * Theme provides an encapsulation of several styles
 * that can be applied to Stylized instances when
 * activated.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Theme {
	private var styles: List[Style] = Nil
	
	/**
	 * Registers a style to be used when this
	 * theme is activated. If this is the currently
	 * active theme revalidation will occur.
	 * 
	 * @param style
	 */
	def register(style: Style) = {
		synchronized {
			styles = style :: styles
		}
		if (isCurrent) {
			apply()
		}
	}
	
	/**
	 * Unregisters a style from being used
	 * with this theme. If this is the currently
	 * active theme revalidation will occur.
	 * 
	 * @param style
	 */
	def unregister(style: Style) = {
		synchronized {
			styles = styles.filterNot(_ == style)
		}
		if (isCurrent) {
			apply()
		}
	}
	
	/**
	 * Convenience method to determine if this is
	 * the currently active theme.
	 * 
	 * @return
	 * 		true if this theme is active
	 */
	def isCurrent = Theme.current() == this
	
	/**
	 * Finds the first matching Style in this Theme
	 * and applies it to stylized. If no match is
	 * found then stylization is removed. Order of
	 * finding is last added to first added.
	 * 
	 * @param stylized
	 */
	def validate(stylized: Stylized) = {
		styles.find(_.condition(stylized)) match {
			case Some(style) => style(stylized)
			case None => Style.remove(stylized)
		}
	}
	
	/**
	 * Convenience method that invokes Stylized(this).
	 */
	def apply() = {
		Stylized(this)
	}
}

object Theme extends PropertyContainer {
	/**
	 * The currently active theme. When this value is
	 * changed the new theme will automatically be applied.
	 * If this is set to null, all styles will be removed.
	 * The default value of this property is null.
	 */
	val current = new AdvancedProperty[Theme](null, this)
	
	current.onEvent[PropertyChangeEvent[_]](ProcessingMode.Blocking) {
		Stylized(current())
	}
}