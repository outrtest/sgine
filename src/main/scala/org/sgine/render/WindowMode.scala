/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sgine.render

import org.sgine.core.Enumerated

sealed trait WindowMode

object WindowMode extends Enumerated[WindowMode] {
   case object Frame extends WindowMode
   case object Applet extends WindowMode
   case object Canvas extends WindowMode
   WindowMode(Frame, Applet, Canvas)
}
