package org.sgine.ui.layout

import org.sgine.ui.Container

trait Layout extends Function1[Container, Unit]

object Layout {
	val default = BoxLayout()
}