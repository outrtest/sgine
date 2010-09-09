package org.sgine.log

case class LogLevel(name: String, value: Int) {
	LogLevel.synchronized {
		LogLevel.named += name -> this
		LogLevel.levels += value -> this
	}
}

object LogLevel {
	private var named = Map.empty[String, LogLevel]
	private var levels = Map.empty[Int, LogLevel]
	
	val Finest = LogLevel("finest", 300)
	val Finer = LogLevel("finer", 400)
	val Fine = LogLevel("fine", 500)
	val Debug = LogLevel("debug", 600)
	val Config = LogLevel("config", 700)
	val Info = LogLevel("info", 800)
	val Warning = LogLevel("warning", 900)
	val Severe = LogLevel("severe", 1000)
	
	val All = 0
	val None = Int.MaxValue
	
	def apply(name: String) = named(name)
	
	def apply(level: Int) = levels(level)
}