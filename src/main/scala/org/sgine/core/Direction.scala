/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sgine.core

sealed trait Direction extends Enumeration

object Direction extends Enumerated[Direction] {
  case object Vertical extends Direction
  case object Horizontal extends Direction
  Direction(Vertical, Horizontal)
}
