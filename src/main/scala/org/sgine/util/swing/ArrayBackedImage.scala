package org.sgine.util.swing

import java.awt.image.{MemoryImageSource, DirectColorModel, BufferedImage}
import java.awt.{Toolkit, Graphics, Image, Color}
import org.sgine.util.DirtyRectangle
import compat.Platform

/**
 * Wraps an image that is backed up by an in-memory array, allowing fast updates.
 *
 * NOTE: This class is not thread safe, access only from one thread at a time.
 */
// TODO: Add thread safety so that different threads can update the image.
// TODO: Maybe some update listening, or in general easier integration with ImagePanel etc.  Update methods should not need to be called by users.
// TODO: Maybe remove alpha support, or make it optional, as it is not needed for rendering thigns in swing, but will slow down image rendering in swing quite a bit.
class ArrayBackedImage(var width: Int, var height: Int) {

  private val BACKGROUND = new Color(0.5f, 0.5f, 0.5f, 1f)
  private val TRANSPARENT = new Color(0.5f, 0.5f, 0.5f, 0f)
  private val TRANSPARENT_COLOR = TRANSPARENT.getRGB()

  private var imageSource: MemoryImageSource = null
  private var image: Image = null
  private var imageData: Array[Int] = null
  private val updatedArea = new DirtyRectangle()

  def getWidth = width
  def getHeight = height

  initialize()

  private def initialize() {
    val rgbColorModel: DirectColorModel = new DirectColorModel(32, 0xff0000, 0x00ff00, 0x0000ff, 0xff000000)

    imageData = new Array[Int](width * height)
    imageSource = new MemoryImageSource(width, height, rgbColorModel, imageData, 0, width)
    imageSource.setAnimated(true)
    image = Toolkit.getDefaultToolkit().createImage(imageSource)

    clear()
  }

  /**
   * Updates the image and returns a reference to it.
   */
  def getImage: Image = {
    updateImage
    image
  }

  /**
   * Makes sure any changes made to the array are reflected in the image.
   * Needs to be used if the image is retrieved with getImage and then modified using the modification operations in this class.
   */
  def updateImage() {
    if (!updatedArea.isEmpty) {
      imageSource.newPixels(updatedArea.getMinX, updatedArea.getMinY, updatedArea.getWidth, updatedArea.getHeight)
      updatedArea.clear
    }
  }

  /**
   * Creates a new BufferedImage based on this images current state.
   */
  def createBufferedImage: BufferedImage = {
    val buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    updateImage
    buf.getGraphics.drawImage(image, 0, 0, null)

    buf
  }

  /**
   *  Renders the image to the specified target.
   */
  def renderImage(context: Graphics) {
    // Clear target background
    context.setColor(BACKGROUND)
    context.fillRect(0, 0, getWidth, getHeight)

    // Render this image to target
    updateImage
    context.drawImage(image, 0, 0, null)
  }

  /**
   * Clears the picture to transparent.
   */
  def clear() {
    clearToColor(TRANSPARENT)
  }

  /**
   * Clears the picture to the specified color.
   */
  def clearToColor(color: Color) {
    require(color != null, "color should not be null")

    val c = color.getRGB
    var i = 0
    while (i < imageData.length) {imageData(i) = c; i += 1}

    updatedArea.includeArea(0, 0, width, height)
  }

  /**
   * Sets the specific pixel to the specified packed RGBA value.
   */
  def putPixel(x: Int, y: Int, colorCode: Int) {
    if (x >= 0 && y >= 0 &&
        x < width && y < height) {

      imageData(x + y * width) = colorCode
      updatedArea.includePoint(x, y)
    }
  }

  /**
   * Adds a block of rgba pixel data directly to the array backing up the image.
   */
  def putData(targetOffset: Int, sourceOffset: Int, size: Int, sourceData: Array[Int]) {
    require(sourceData != null, "sourceData should not be null")
    require(size >= 0, "size should not be negative")
    require(targetOffset >= 0, "targetOffset should not be negative")
    require(sourceOffset >= 0, "sourceOffset should not be negative")
    require(sourceOffset + size <= sourceData.length, "sourceOffset + size should be within the sourceData lenght")
    require(targetOffset + size <= imageData.length, "targetOffset + size should be within the imageData lenght")

    Platform.arraycopy(sourceData, sourceOffset, imageData, targetOffset, size)
    updatedArea.includeElementBlock(targetOffset, size, width, height)
  }

}

