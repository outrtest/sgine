package org.sgine.render.shape

import java.util.concurrent.atomic.AtomicReference

import org.sgine.render.shape.renderer._

trait Shape extends Function0[Unit] {
	private val _data = new AtomicReference[ShapeData]
	private val _update = new AtomicReference[ShapeData]

	private lazy val renderer = createRenderer()
	
	def dirty = _update.get != null
	
	def apply() = {
		// Updates
		var data = _update.getAndSet(null)
		if (data != null) {
			renderer.update(_data.get, data)
			_data.set(data)
		}
		
		// Render
		data = _data.get
		if (data != null) {
			renderer.render(data)
		}
	}
	
	def apply(data: ShapeData) = _update.set(data)
	
	private def createRenderer(): ShapeRenderer = {
		if (LWJGLVBOShapeRenderer.capable) {
			new LWJGLVBOShapeRenderer()
		} else {
			new LWJGLImmediateModeShapeRenderer()
		}
	}
}