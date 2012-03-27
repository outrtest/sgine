package org.sgine.datastore

import java.util.UUID

/**
 * Identifiable represents a unique identifiable object.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Identifiable {
  /**
   * @return universally unique identifier for this instance
   */
  def id: UUID
}
