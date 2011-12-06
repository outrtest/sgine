package org.sgine

import annotation.tailrec

/**
 * Updater contains a list of Updatables that are updated upon upon the update of this class.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Updater extends Updatable {
  private var updatables: List[Updatable] = Nil

  override def update(delta: Double) = {
    super.update(delta)

    doUpdate(delta, updatables)
  }

  @tailrec
  private def doUpdate(delta: Double, list: List[Updatable]): Unit = {
    if (!list.isEmpty) {
      val u = list.head
      u.update(delta)
      u match {
        case f: Finishable if (f.isFinished) => remove(u)
        case _ =>
      }
      doUpdate(delta, list.tail)
    }
  }

  protected def add(updatable: Updatable) = synchronized {
    updatables = updatable :: updatables
  }

  protected def remove(updatable: Updatable) = synchronized {
    updatables = updatables.filterNot(u => u == updatable)
  }
}