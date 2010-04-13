package org.sgine.util.swing

import java.awt.Dimension
import javax.swing.{JComponent, JFrame}


/**
 * A JFrame implementation with sensible default settings.
 * Makes it easier to do quick UI prototyping.
 *
 * The SimpleFrame is shown automatically as it is created, no separate call to show is necessary.
 */
class SimpleFrame(title : String, content : JComponent) extends JFrame {
  setTitle(title)
  setContentPane( content )
  setPreferredSize( new Dimension(800, 600) )

  setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE )

  pack
  setVisible(true)
}

