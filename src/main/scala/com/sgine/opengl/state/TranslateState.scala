package com.sgine.opengl.state


import com.sgine.math._;
import com.sgine.opengl.GLContext._;
import com.sgine.opengl.generated.OpenGL2._;

class TranslateState private(val p:Vector3 = Vector3.Zero) extends Function1[Double, Unit] {
	def apply(time:Double) = {
		glTranslated(p.x, p.y, p.z);
	}
}

object TranslateState {
	def apply(p:Vector3 = Vector3.Zero):TranslateState = new TranslateState(p);
	
	def apply(x:Double, y:Double, z:Double):TranslateState = TranslateState(Vector3(x, y, z));
}