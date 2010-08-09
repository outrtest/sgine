package org.sgine.log

import org.sgine.core.Enumeration
import org.sgine.core.Enumerated

sealed trait FilterMode extends Enumeration

object FilterMode extends Enumerated[FilterMode] {
  case object AnyMatch extends FilterMode
  case object AllMatch extends FilterMode
  FilterMode(AnyMatch, AllMatch)
}