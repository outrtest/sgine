package com.sgine.opengl

import point.Point2D

package object point {
	implicit def t2top2d(value:(Double, Double)) = Point2D(value._1, value._2);
}
