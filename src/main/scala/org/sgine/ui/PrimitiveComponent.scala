package org.sgine.ui

import ext.AdvancedComponent
import org.sgine.event.{ProcessingMode, Event, EventHandler}
import org.sgine.render.primitive.Primitive

/*
 * Common trait for primitive 3D shapes.
 * Wraps a Primitive instance that handles the rendering.
 */
trait PrimitiveComponent extends AdvancedComponent {

  private var primitive: Primitive = null

  /*
   * Create the primitive using current property values.
   */
  protected def createPrimitive(): Primitive

  /*
   * Forces a re-creation of the primitive the next time it is to be drawn.
   * Use e.g. if you change properies that affect the shape of the primitive.
   */
  protected def invalidatePrimitive() {
    primitive = null;
  }

  /*
   * Convenience event handler that can be added as a listener to a property that should cause the primitive to be updated when changed.
   */
  protected val invalidationHandler: EventHandler = EventHandler((e:Event) => invalidatePrimitive, ProcessingMode.Blocking)

  protected[ui] def drawComponent() {
    if (primitive == null) primitive = createPrimitive()

    // Draw primitive
    primitive()
  }

}