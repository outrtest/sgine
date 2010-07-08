package org.sgine.render.spatial.renderer

import org.sgine.render.spatial.MeshData

trait SpatialRenderer {
	/**
	 * Invoked when there is replacement mesh data
	 * to be applied. This method will always be
	 * invoked in the GL thread and <code>mesh</code>
	 * will always be non-null.
	 * 
	 * The value <code>old</code> will represent the
	 * MeshData that is being replaced, or null if this
	 * is the first update.
	 * 
	 * @param old
	 * @param mesh
	 */
	protected[spatial] def update(old: MeshData, mesh: MeshData): Unit
	
	/**
	 * Invoked to render mesh data to the screen.
	 * This method will always be invoked in the
	 * GL thread and <code>mesh</code> will always
	 * be non-null.
	 * 
	 * @param mesh
	 */
	protected[spatial] def render(mesh: MeshData): Unit
}