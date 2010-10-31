package org.sgine.ui

class InstanceManager extends AbstractContainer {
	private var map = Map.empty[String, Instances]
	
	_layout := null
	visible := false
	
	def register(name: String, c: Component) = {
		// Add it to the scene
//		c.visible := false
		this += c
		
		// Make it available in the map
		synchronized {
			map += name -> new Instances(c)
		}
	}
	
	def request(name: String) = map(name).request()
	
	def release(name: String, instance: ComponentInstance) = map(name).release(instance)
	
	class Instances(original: Component) {
		private var queue = new java.util.concurrent.ConcurrentLinkedQueue[ComponentInstance]
		
		def request() = {
			queue.poll match {
				case null => ComponentInstance(original)
				case instance => instance
			}
		}
		
		def release(instance: ComponentInstance) = {
			queue.add(instance)
		}
	}
}