package org.sgine.util.swing

import javax.swing.JPanel
import java.awt.{Graphics, Image}

/**
 * A Swing component that displays a given Image.
 */
case class ImagePanel(image: Image) extends JPanel {

  override def paintComponent(g: Graphics) = {
    g.drawImage(image, 0, 0, null)
  }
}
