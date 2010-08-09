package org.sgine.ui.ext

import org.sgine.ui.Component

trait PaddingComponent extends Component {
	val padding = new Padding(this)
}