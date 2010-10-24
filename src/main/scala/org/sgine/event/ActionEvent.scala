package org.sgine.event

case class ActionEvent(_listenable: Listenable, action: String) extends BasicEvent(_listenable)