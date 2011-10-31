package org.sgine

import naming.NamedChild

/**
 * EnumEntries should be instanced within an Enumerated companion object.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait EnumEntry[T] extends NamedChild {
  lazy val parent = companion.asInstanceOf[Enumerated[_]]

  override def equals(obj: Any) = obj match {
    case c: Combined[_] => c.equals(this)
    case _ => super.equals(obj)
  }
}