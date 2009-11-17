package com.sgine.opengl

import com.sgine.opengl.point._

package object point {
	implicit def t2top2d(value:(Double, Double)) = Point2(value._1, value._2);
	implicit def t2top3d(value:(Double, Double)) = Point3(value._1, value._2);
	implicit def t3top3d(value:(Double, Double, Double)) = Point3(value._1, value._2, value._3);
}
