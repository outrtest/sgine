/*
 * Copyright (c) 2011 Sgine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 *  Neither the name of 'Sgine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sgine.ui

import org.sgine.render.{Vertices, Shape}
import org.sgine.event.ChangeEvent
import org.sgine.resource.Resource

import org.sgine.property._

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Image extends ShapeComponent with TexturedComponent with TranslationMatrixComponent {
  val width = new MutableProperty[Double](0.0)
  val height = new MutableProperty[Double](0.0)

  texture.change.synchronous {
    case evt => {
      width := texture.width
      height := texture.height
    }
  }

  protected lazy val shape = Shape(Vertices.rect(width(), height()))

  // Update the shape vertices when width or height changes
  dirtyUpdate[ChangeEvent[Double]]("imageSize", width, height) {
    shape.updateVertices(Vertices.rect(width(), height()))
  }
}

object Image {
  def apply(resource: Resource, mipmap: Boolean = true) = {
    val image = new Image()
    val texture = Texture(resource, mipmap)
    image.texture := texture
    image
  }
}