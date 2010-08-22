package org.sgine.render.shape.renderer

import org.sgine.render.shape._

trait ShapeRenderer {
	/**
	 * Invoked when there is replacement data
	 * to be applied. This method will always be
	 * invoked in the GL thread and <code>data</code>
	 * will always be non-null.
	 * 
	 * The value <code>old</code> will represent the
	 * ShapeData that is being replaced, or null if this
	 * is the first update.
	 * 
	 * @param old
	 * @param data
	 */
	protected[shape] def update(shape: Shape, vertexChanged: Boolean = false, colorChanged: Boolean = false, textureChanged: Boolean = false, normalChanged: Boolean = false): Unit
	
	/**
	 * Invoked to render data to the screen.
	 * This method will always be invoked in the
	 * GL thread and <code>shape</code> will always
	 * be non-null.
	 * 
	 * @param shape
	 */
	protected[shape] def render(shape: Shape): Unit
}