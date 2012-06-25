package org.sgine.datastore

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Persistable extends Identifiable {
  protected[datastore] var _state: PersistenceState = PersistenceState.NotPersisted

  def state = _state
}

sealed class PersistenceState

object PersistenceState {
  val NotPersisted = new PersistenceState
  val Persisted = new PersistenceState
}