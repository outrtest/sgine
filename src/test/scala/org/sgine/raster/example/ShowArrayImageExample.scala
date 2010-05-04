package org.sgine.raster.example

import org.sgine.util.swing.{ArrayBackedImage, ImagePanel, SimpleFrame}
import java.awt.Color
import scala.Math._

/**
 * Demonstrates creating and showing an ArrayImage in a Swing UI.
 */
object ShowArrayImageExample {
  def main(args: Array[String]) {

    val arrayImage = new ArrayBackedImage(512, 512)
    val imagePanel: ImagePanel = new ImagePanel(arrayImage.getImage)
    new SimpleFrame("Show Raster Exmple", imagePanel)

    drawTestImage(arrayImage)

    arrayImage.updateImage() // TODO: The need for this should be eliminated
    imagePanel.repaint() // TODO: The need for this should also be eliminated
  }

  def drawTestImage(arrayImage: ArrayBackedImage) {
    arrayImage.clearToColor(new Color(1f, 0.8f, 0.9f))
    for (i <- 1 to 512) {
      arrayImage.putPixel(i, i, Color.BLACK.getRGB)
      arrayImage.putPixel(512-i, i, Color.BLACK.getRGB)
    }
  }
}
