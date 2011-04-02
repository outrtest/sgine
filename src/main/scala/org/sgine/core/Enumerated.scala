package org.sgine.core

trait Enumerated[E <: Enum] extends Traversable[E] {
  private var list: List[E] = _
  
  lazy val values: Seq[E] = list

  def apply(values: E*) { list = values.toList }

  def apply(index: Int): E = values(index)

  override lazy val size: Int = values.size

  def foreach[U](f: E => U) = values foreach f

  def valueOf(s: String): Option[E] = values.find(s == _.name)

  def indexOf(e: E): Int = values.indexOf(e)
  
  def indexOf(s: String): Int = values.find(s == _.name) match {
	  case Some(e) => indexOf(e)
	  case None => -1
  }
  
  def next(current: E) = apply(if (current.ordinal == size - 1) 0 else current.ordinal + 1)
  
  def previous(current: E) = apply(if (current.ordinal == 0) size - 1 else current.ordinal - 1)
  
  def random = values(new scala.util.Random().nextInt(values.length))
}