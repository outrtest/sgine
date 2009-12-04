package com.sgine.property.group

import com.sgine._;
import com.sgine.property._;

class PropertyGroup extends Updatable {
	lazy val properties:List[Property[_]] = propertiesList()
	
	def update(time:Double) = {
		for (p <- properties) p match {
			case u:Updatable => u.update(time)
			case _ =>
		}
	}
	
	private def propertiesList():List[Property[_]] = {
		var props:List[Property[_]] = Nil
		
		for (f <- getClass.getDeclaredFields) {
			f.setAccessible(true)
			
			f.get(this) match {
				case p:Property[_] => {
					props = p :: props
				}
				case _ =>
			}
		}
		
		props
	}
}