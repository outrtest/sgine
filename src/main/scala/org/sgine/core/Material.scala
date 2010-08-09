package org.sgine.core

sealed trait Material extends Enumeration

object Material extends Enumerated[Material] {
	case object Emission extends Material
	case object Ambient extends Material
	case object Diffuse extends Material
	case object Specular extends Material
	case object AmbientAndDiffuse extends Material
	
	Material(Emission, Ambient, Diffuse, Specular, AmbientAndDiffuse)
}