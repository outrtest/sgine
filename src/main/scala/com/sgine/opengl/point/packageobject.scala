package com.sgine.opengl

package object point {
	implicit def t2top2d(value:(Double, Double)) = Point2d(value._1, value._2);
	implicit def t2top3d(value:(Double, Double)) = Point3d(value._1, value._2);
	implicit def t3top3d(value:(Double, Double, Double)) = Point3d(value._1, value._2, value._3);
}
