import org.sgine.property.Property;

package object property {
	implicit def propertyToValue[T](p:Property[T]):T = p();
}