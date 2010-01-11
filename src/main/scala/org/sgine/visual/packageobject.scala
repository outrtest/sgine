import org.sgine.visual._
import org.sgine.visual.material._

package object visual {
	implicit def pigmentToMaterial(value: Pigment) = Material(value)
}