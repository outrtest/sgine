package org.sgine.math.property

import org.sgine.property.ChangeableProperty
import org.sgine.property.FilteredProperty
import org.sgine.property.Property

import simplex3d.math.doublem.renamed._

trait MatrixFilteredProperty extends FilteredProperty[ReadMat3x4] {
	def filter(value: ReadMat3x4) = {
		apply().asInstanceOf[Mat3x4] := value
		apply()
	}
	
	abstract override def apply(value: ReadMat3x4): Property[ReadMat3x4] = {
		super.apply(value)
		
		this match {
			case cp: ChangeableProperty[ReadMat3x4] => cp.changed(apply(), apply())
			case _ =>
		}
		
		this
	}
}