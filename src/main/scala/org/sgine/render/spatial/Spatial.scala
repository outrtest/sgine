package org.sgine.render.spatial

import java.util.concurrent.atomic.AtomicReference

import org.sgine.render.spatial.renderer._

trait Spatial extends Function0[Unit] {
	private val _data = new AtomicReference[MeshData]
	private val _update = new AtomicReference[MeshData]

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
	
	def apply(mesh: MeshData) = _update.set(mesh)
	
	private def createRenderer(): SpatialRenderer = {
		if (VBOSpatialRenderer.capable) {
			new VBOSpatialRenderer()
		} else {
			new ImmediateModeSpatialRenderer()
		}
	}
}