package org.sgine.workflow

import org.sgine.{Enumerated, EnumEntry}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed class Repeat extends EnumEntry[Repeat]

object Repeat extends Enumerated[Repeat] {
  val All = new Repeat()
  val First = new Repeat()
  val Last = new Repeat()
}