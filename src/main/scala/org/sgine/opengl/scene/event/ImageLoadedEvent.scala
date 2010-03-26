package org.sgine.opengl.scene.event

import org.sgine.event._
import org.sgine.opengl.scene._

class ImageLoadedEvent(val image: GLImage) extends Event(image)