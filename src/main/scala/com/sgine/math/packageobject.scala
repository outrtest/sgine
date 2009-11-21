import com.sgine.math.{Fixed, Vector2, Vector3}

package object math {
	implicit def tuple3dToVector3(value:(Double, Double, Double)) : Vector3 = new Vector3(value._1, value._2, value._3);
	implicit def tuple3iToVector3(value:(Int,    Int,    Int   )) : Vector3 = new Vector3(value._1, value._2, value._3);

	implicit def tuple2dToVector2(value:(Double, Double)) : Vector2 = new Vector2(value._1, value._2);
	implicit def tuple2iToVector2(value:(Int,    Int   )) : Vector2 = new Vector2(value._1, value._2);

  implicit def int2fixed( v : Int ) : Fixed = Fixed( v << Fixed.ShiftAmount )
  implicit def long2fixed( v : Long ) : Fixed = Fixed( v << Fixed.ShiftAmount )
  implicit def float2fixed( v : Float ) : Fixed = Fixed( (v * Fixed.factorF).toLong )
  implicit def double2fixed( v : Double ) : Fixed = Fixed( (v * Fixed.factorD).toLong )

}