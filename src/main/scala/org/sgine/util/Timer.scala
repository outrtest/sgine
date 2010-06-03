package org.sgine.util

import scala.collection.mutable.HashMap

class Timer private() {
	private val map = new HashMap[String, Long]()
	private val local = new ThreadLocal[HashMap[String, Long]]() {
		override protected def initialValue() = new HashMap[String, Long]()
	}
	
	def start(name: String) = {
		val lm = local.get()
		lm(name) = System.currentTimeMillis
	}
	
	def stop(name: String) = {
		add(name, System.currentTimeMillis - local.get()(name))
	}
	
	def get(name: String) = map.getOrElse(name, 0L)
	
	def add(name: String, value: Long) = {
		synchronized {
			val current = map.getOrElse(name, 0L)
			map(name) = current + value
		}
	}
	
	def reset(name: String) = {
		synchronized {
			map(name) = 0L
		}
	}
}

object Timer {
	def apply() = new Timer()
	
	def main(args: Array[String]): Unit = {
		val t = Timer()
		t.start("First")
		t.start("Second")
		Thread.sleep(1000)
		t.stop("First")
		Thread.sleep(1000)
		t.stop("Second")
		t.start("First")
		Thread.sleep(1000)
		t.stop("First")
		println("First: " + t.get("First"))
		println("Second: " + t.get("Second"))
	}
}