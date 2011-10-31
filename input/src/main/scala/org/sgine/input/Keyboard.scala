package org.sgine.input

import event.KeyEventSupport
import org.sgine.event.Listenable

/**
 * Keyboard represents a listenable object to hear all events that happen on the keyboard.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Keyboard extends Listenable with KeyEventSupport