package org.sgine

import org.sgine.core.Color
import org.sgine.core.DepthAlignment
import org.sgine.core.Direction
import org.sgine.core.HorizontalAlignment
import org.sgine.core.Placement
import org.sgine.core.Resource
import org.sgine.core.VerticalAlignment

package object core {
	implicit def stringToColor(value: String): Color = Color(value)
	
	implicit def stringToDepthAlignment(value: String) = value.toLowerCase match {
		case "front" => DepthAlignment.Front
		case "middle" => DepthAlignment.Middle
		case "back" => DepthAlignment.Back
		case _ => throw new RuntimeException("Cannot convert '" + value + "' to a DepthAlignment.")
	}
	
	implicit def stringToDirection(value: String) = value.toLowerCase match {
		case "vertical" => Direction.Vertical
		case "horizontal" => Direction.Horizontal
		case _ => throw new RuntimeException("Cannot convert '" + value + "' to a Direction.")
	}
	
	implicit def stringToHorizontalAlignment(value: String) = value.toLowerCase match {
		case "left" => HorizontalAlignment.Left
		case "center" => HorizontalAlignment.Center
		case "right" => HorizontalAlignment.Right
		case _ => throw new RuntimeException("Cannot convert '" + value + "' to a HorizontalAlignment.")
	}
	
	implicit def stringToPlacement(value: String) = value.toLowerCase match {
		case "top" => Placement.Top
		case "bottom" => Placement.Bottom
		case "left" => Placement.Left
		case "right" => Placement.Right
		case "middle" => Placement.Middle
		case _ => throw new RuntimeException("Cannot convert '" + value + "' to a Placement.")
	}
	
	implicit def stringToResource(value: String) = Resource(value)
	
	implicit def stringToVerticalAlignment(value: String) = value.toLowerCase match {
		case "top" => VerticalAlignment.Top
		case "middle" => VerticalAlignment.Middle
		case "bottom" => VerticalAlignment.Bottom
		case _ => throw new RuntimeException("Cannot convert '" + value + "' to a VerticalAlignment.")
	}
}