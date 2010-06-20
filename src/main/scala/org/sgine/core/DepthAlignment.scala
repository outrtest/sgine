package org.sgine.core

sealed trait DepthAlignment

object DepthAlignment extends Enumerated[DepthAlignment] {
  case object Front extends DepthAlignment
  case object Middle extends DepthAlignment
  case object Back extends DepthAlignment
  DepthAlignment(Front, Middle, Back)
}