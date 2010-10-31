package org.sgine.ui.layout

import org.sgine.ui.AbstractContainer

trait Layout extends Function1[AbstractContainer, Unit]

object Layout {
	val default = null
}