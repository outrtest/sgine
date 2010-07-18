package org.sgine.ui.layout

import org.sgine.scene.NodeContainer

trait Layout extends Function1[NodeContainer, Unit]

object Layout {
	val default = BoxLayout()
}