package org.sgine

/**
 * Finishable simply defines a method "isFinished" that returns true if the instance is complete.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Finishable {
  def isFinished: Boolean
}