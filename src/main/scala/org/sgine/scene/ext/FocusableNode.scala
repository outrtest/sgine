package org.sgine.scene.ext

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.input.Key
import org.sgine.input.Keyboard
import org.sgine.input.event.KeyReleaseEvent

import org.sgine.property.AdvancedProperty
import org.sgine.property.DelegateProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.scene.Node

trait FocusableNode extends Node {
	val focused = new AdvancedProperty[Boolean](false, this)
	val focusable = new AdvancedProperty[Boolean](true, this)
	
	focused.listeners += EventHandler(focusChanged, ProcessingMode.Blocking)
	
	private def focusChanged(evt: PropertyChangeEvent[Boolean]) = {
		if (evt.newValue) {
			FocusableNode.focus(this)
		} else {
			FocusableNode.blur(this)
		}
	}
}

object FocusableNode {
	val focused = new DelegateProperty[FocusableNode](() => _focused)
	val enabled = new AdvancedProperty[Boolean](true)
	
	private var _focused: FocusableNode = _
	
	Keyboard.listeners += EventHandler(keyReleased, ProcessingMode.Blocking)
	
	def focus(n: FocusableNode) = {
		val current = focused()
		if (current != null) {
			blur(current)
		}
		if (focused() != n) _focused = n
		if (!n.focused()) n.focused := true
	}
	
	def blur(n: FocusableNode) = {
		val current = focused()
		if (current == n) {
			if (n.focused()) n.focused := false
			
			_focused = null
		}
	}
	
	private def keyReleased(evt: KeyReleaseEvent) = {
		val f = focused()
		if (f != null) {
			if (evt.key == Key.Tab) {
				if (evt.shiftDown) {
					f.previous(focusableTest) match {
						case null => f.lastNode.previous(focusableTest) match {
							case null =>
							case n: FocusableNode => n.focused := true
						}
						case n: FocusableNode => n.focused := true
					}
				} else {
					f.next(focusableTest) match {
						case null => f.root.next(focusableTest) match {
							case null =>
							case n: FocusableNode => n.focused := true
						}
						case n: FocusableNode => n.focused := true
					}
				}
			}
		}
	}
	
//	private val focusableTest = (n: Node) => n.isInstanceOf[FocusableNode]
	
	private val focusableTest = (n: Node) => n match {
		case fn: FocusableNode => fn.focusable()
		case _ => false
	}
}