package org.sgine.ui.font

import scala.math._

/**
 * TextGenerator works with a BitmapFont to place glyphs positionally using wrapping.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class TextGenerator(var font: BitmapFont) {
  type Drawer = (Double, Double, BitmapFontGlyph) => Unit

  private var x = 0.0
  private var y = 0.0
  private var maxWidth = 0.0
  private val b = new StringBuilder

  var drawer: Drawer = _
  var wrap = Double.MaxValue
  var kerning = true

  def measure(text: String) = synchronized {
    drawer = null
    process(text)
    size
  }

  def draw(text: String, drawer: Drawer) = synchronized {
    this.drawer = drawer
    process(text)
  }

  private def process(text: String) = {
    // Reset
    x = 0.0
    y = 0.0
    maxWidth = 0.0
    b.clear()

    // Process text
    for (c <- text) {
      if (c == '\n') {
        drawCurrent()
        createNewLine()
      } else if (c.isWhitespace) {
        drawCurrent()
        b.append(c)
        drawCurrent(true)   // Draw the space on the current line
      } else {
        b.append(c)
      }
    }
    if (!b.isEmpty) {
      drawCurrent()
      maxWidth = max(maxWidth, x)
      y -= font.lineHeight
    }
  }

  private def drawCurrent(noBreak: Boolean = false) = if (!b.isEmpty) {
    val width = processBlock(b.toString(), null)
    if (x > 0.0 && width > wrap) {
      createNewLine()
    }
    processBlock(b.toString(), drawer, x, y)
    x += width
    b.clear()
  }

  private def createNewLine() = {
    maxWidth = max(x, maxWidth)
    x = 0.0
    y -= font.lineHeight
  }

  def size = maxWidth -> -y

  private def processBlock(text: String,
              drawer: Drawer,
              xOffset: Double = 0.0,
              yOffset: Double = 0.0) = {
    var x = xOffset
    var y = yOffset - font.lineHeight
    var p: BitmapFontGlyph = null
    for (c <- text) {
      val glyph = font.glyphs(c)
      if (kerning && p != null) {
        x += font.kerning(p.id, c)
      }
      if (drawer != null) {
        drawer(x, y, glyph)
      }
      x += glyph.xAdvance
    }
    x
  }
}