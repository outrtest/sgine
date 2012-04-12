package org.sgine


/**
 * EnumEntries should be instanced within an Enumerated companion object.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
abstract class EnumEntry[E <: EnumEntry[E]](implicit val parent: Enumerated[E]) {
  parent.add(this.asInstanceOf[E])

  /**
   * The name of this EnumEntry.
   */
  lazy val name = parent.name(this.asInstanceOf[E])
  /**
   * The index of this EnumEntry.
   */
  final lazy val ordinal = parent.index(this.asInstanceOf[E])

  override def toString = if (parent != null) {
    "%s.%s".format(parent.name, name)
  } else {
    name
  }
}