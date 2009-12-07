package org.sgine.raster

import java.awt.image.BufferedImage

/**
 * Contains a two dimensional array of values.
 *
 * Can be used to store a texture, picture, heightmap, etc.
 *
 * Provides conversations to a Texture or a Swing Image.
 */
trait Raster {

  /**
   * Width of raster.
   */
  def width: Int

  /**
   * Height of raster.
   */
  def height: Int

  /**
   * Number of channels.
   */
  def channelCount: Int

  /**
   * The identifiers and descriptions of the channels that this raster has.
   */
  def channels: List[ParameterInfo]

  /**
   * Gets the value at the given position in the specified channel.
   */
  def get(channel: Symbol, x: Int, y: Int): Float

  /**
   * Sets the value at the given position in the specified channel.
   */
  def set(channel: Symbol, x: Int, y: Int, value: Float)

  /**
   * The backing array for the specified channel, arranged in increasing row order.
   * Changes to the array will change the Raster.
   */
  def getChannel(channel: Symbol): Array[Float]

  /**
   * A BufferedImage copy of the raster.
   */
  def toImage: BufferedImage

  /* TODO
    def toTexture
  */

/* TODO: Extractors for one channel or all channels as a float array (or buffer)?.
  def toArray
*/
}
