import com.sgine.math.Vec

package object math {
	implicit def tuple3dToVector3d(value:(Double, Double, Double)) : Vec = Vec(value._1, value._2, value._3);
	implicit def tuple3iToVector3d(value:(Int, Int, Int)) : Vec = Vec(value._1, value._2, value._3);
}
