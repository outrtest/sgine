package org.sgine.render

import org.lwjgl.opengl.GL11._

import org.sgine.core._

import org.sgine.event.Event

import org.sgine.property.AdvancedProperty
import org.sgine.property.MutableProperty

import org.sgine.render.scene.RenderableQuery

import org.sgine.scene.Node
import org.sgine.scene.view.NodeView

trait CacheableRenderable extends Renderable {
	private val revalidateCache = new MutableProperty[Boolean](true)
	private val nodeView = NodeView(this, RenderableQuery, ProcessingMode.Blocking, null)
	
	private var listId: Int = _
	
	def invalidateCache(evt: Event = null) = {
		revalidateCache := true
	}
	
	/**
	 * Determines the caching state of the Renderable. If set to true rendering will
	 * be cached once and then only updated upon change or demand.
	 */
	val cache = new AdvancedProperty[Boolean](false, this)
	
	/**
	 * Set to true for the thread that is invoking the caching update.
	 */
	protected[render] val caching = new ThreadLocal[Boolean] {
		override def initialValue() = false
	}
	
	private var renderer: Renderer = _
	
	def render(renderer: Renderer) = {
		if ((revalidateCache()) && (cache())) {
			revalidateCache := false
			
			caching.set(true)
			
			// Set up list
			listId = glGenLists(1)
			glNewList(listId, GL_COMPILE)
			
			// Render
			this.renderer = renderer
			nodeView.foreach(renderItem)
			
			// End List
			glEndList()
			
			caching.set(false)
		} else if (cache()) {
			glCallList(listId)
		}
	}
	
	private val renderItem = (n: Node) => {
		Renderable.render(renderer, n.asInstanceOf[Renderable])
	}
}