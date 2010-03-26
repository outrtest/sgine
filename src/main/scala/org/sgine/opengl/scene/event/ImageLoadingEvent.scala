package org.sgine.opengl.scene.event

import org.sgine.event._
import org.sgine.opengl.scene._

class ImageLoadingEvent(val image: GLImage) extends Event(image)