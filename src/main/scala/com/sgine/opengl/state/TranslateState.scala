package com.sgine.opengl.state

import com.sgine.opengl.point._;

import com.sgine.opengl.GLContext._;
import com.sgine.opengl.generated.OpenGL2._;

class TranslateState private(val p:Point3d = (0.0, 0.0, 0.0)) extends Function1[Double, Unit] {
	def apply(time:Double) = {
		glTranslated(p.x, p.y, p.z);
	}
}

object TranslateState {
	def apply(p:Point3d = (0.0, 0.0, 0.0)):TranslateState = new TranslateState(p);
	
	def apply(x:Double, y:Double, z:Double):TranslateState = TranslateState((x, y, z));
}