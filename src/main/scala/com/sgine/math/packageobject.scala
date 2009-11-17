import com.sgine.math.Vector3

package object math {
	implicit def tuple3dToVector3d(value:(Double, Double, Double)) : Vector3 = Vector3(value._1, value._2, value._3);
	implicit def tuple3iToVector3d(value:(Int, Int, Int)) : Vector3 = Vector3(value._1, value._2, value._3);
}
