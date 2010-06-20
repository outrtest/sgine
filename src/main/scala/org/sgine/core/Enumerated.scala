/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sgine.core

abstract trait Enumerated[E] extends Traversable[E] {

  private var list : List[E] = _

  lazy val values : Seq[E] = list

  def apply(values : E*) { list = values.toList }

  def apply(index : Int) : E = values(index)

  override lazy val size : Int = values.size

  def foreach[U](f : E => U) = values foreach f

  def valueOf(s : String) : Option[E] = values.find(s == _.toString)

  def indexOf(e : E) : Int = values.indexOf(e)

}