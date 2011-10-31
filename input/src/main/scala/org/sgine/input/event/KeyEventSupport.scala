package org.sgine.input.event

import org.sgine.event.{EventSupport, Listenable}


/**
 * KeyEventSupport
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait KeyEventSupport extends Listenable {
  def keyEvent = KeyEventSupport(this)
}

object KeyEventSupport extends EventSupport[KeyEvent]