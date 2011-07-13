package org.sgine

package object resource {
  implicit def string2Resource(s: String) = Resource(s)
}