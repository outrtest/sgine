package org.sgine.datastore

import java.util.UUID

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Identifiable {
  /**
   * @return universally unique identifier for this instance
   */
  def id: UUID
}
