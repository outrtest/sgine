package org.sgine

import org.sgine.math.{Vector2, Vector3}

package object math {
	implicit def tuple3dToVector3(value:(Double, Double, Double)) : Vector3 = Vector3(value._1, value._2, value._3);
	implicit def tuple3iToVector3(value:(Int,    Int,    Int   )) : Vector3 = Vector3(value._1, value._2, value._3);

	implicit def tuple2dToVector2(value:(Double, Double)) : Vector2 = Vector2(value._1, value._2);
	implicit def tuple2iToVector2(value:(Int,    Int   )) : Vector2 = Vector2(value._1, value._2);
	
	implicit def stringToMutability(value: String) = if (value equalsIgnoreCase "mutable") Mutability.Mutable else Mutability.Immutable
	
	implicit def stringToStoreType(value: String) = if (value equalsIgnoreCase "direct") StoreType.DirectBuffer else if (value equalsIgnoreCase "buffer") StoreType.Buffer else StoreType.BasicArray
}