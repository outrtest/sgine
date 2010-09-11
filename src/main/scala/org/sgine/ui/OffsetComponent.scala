package org.sgine.ui

import org.sgine.ui.ext.Actual

trait OffsetComponent extends Component {
	val offset = new Actual(this)
}