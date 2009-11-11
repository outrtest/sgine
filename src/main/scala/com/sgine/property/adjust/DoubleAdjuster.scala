package com.sgine.property.adjust

trait DoubleAdjuster extends PropertyAdjuster[Double] {
	val toDouble:Function1[Double, Double] = convert
	val fromDouble:Function1[Double, Double] = convert
	
	private def convert(in:Double):Double = in
}
