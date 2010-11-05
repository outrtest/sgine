package org.sgine.ui

class InstanceManager extends AbstractContainer {
	private var map = Map.empty[String, Instances]
	
	_layout := null
	visible := false
	
	def drawComponent() = {
	}

	def register(name: String, c: Component, creator: Component => Component = InstanceManager.defaultCreator) = {
		// Add it to the scene
		this += c
		
		// Make it available in the map
		synchronized {
			map += name -> new Instances(c, creator)
		}
	}
	
	def instance(name: String) = map(name)
	
	def request(name: String) = map(name).request()
	
	def release(name: String, instance: ComponentInstance) = map(name).release(instance)
	
	class Instances(val original: Component, val creator: Component => Component) {
		private var queue = new java.util.concurrent.ConcurrentLinkedQueue[ComponentInstance]
		
		def request() = {
			queue.poll match {
				case null => creator(original)
				case instance => instance
			}
		}
		
		def release(instance: ComponentInstance) = {
			queue.add(instance)
		}
	}
}

object InstanceManager {
	val defaultCreator = (original: Component) => ComponentInstance(original)
}