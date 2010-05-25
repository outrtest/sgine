package org.sgine.render

import org.sgine.scene.Node

trait Renderable extends Node {
	protected def render(renderer: Renderer)
	
	def shouldRender = {
		if (Renderable.isCached(parent)) {
			false
		} else {
			true
		}
	}
}

object Renderable {
	def render(renderer: Renderer, r: Renderable) = {
		if (r.shouldRender) {
			r.render(renderer)
		}
	}
	
	def isCached(n: Node): Boolean = {
		n match {
			case null => false
			case cr: CacheableRenderable => {
				if (cr.cache()) {
					if (cr.caching.get()) {
						false
					} else {
						true
					}
				} else {
					isCached(n.parent)
				}
			}
			case _ => isCached(n.parent)
		}
	}
}