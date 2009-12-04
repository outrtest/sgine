package com.sgine.easing

object Sine {
	def easeIn(time:Double, start:Double, change:Double, duration:Double):Double = {
		-change * Math.cos(time / duration * (Math.Pi / 2.0)) + change + start
	}
	
	def easeOut(time:Double, start:Double, change:Double, duration:Double):Double = {
		change * Math.sin(time / duration * (Math.Pi / 2.0)) + start
	}
	
	def easeInOut(time:Double, start:Double, change:Double, duration:Double):Double = {
		-change / 2.0 * (Math.cos(Math.Pi * time / duration) - 1.0) + start
	}
}