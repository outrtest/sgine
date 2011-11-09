package org.sgine

/**
 * Updatable represents a class that gets updated with the delta between the last update and the
 * current update.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Updatable {
  def update(delta: Double) = {
  }
}