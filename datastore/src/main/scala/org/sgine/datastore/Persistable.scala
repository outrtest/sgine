package org.sgine.datastore

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Persistable extends Identifiable {
  protected[datastore] var _persistanceState: PersistenceState = PersistenceState.NotPersisted

  def persistenceState = _persistanceState
}

sealed class PersistenceState

object PersistenceState {
  val NotPersisted = new PersistenceState
  val Persisted = new PersistenceState
}