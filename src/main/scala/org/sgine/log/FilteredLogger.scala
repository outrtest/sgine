package org.sgine.log

trait FilteredLogger extends Logger {
	var filters: List[(Log) => Boolean] = Nil
	var filterMode: FilterMode = FilterMode.AllMatch
	
	abstract override protected[log] def valid(log: Log) = {
		if (super.valid(log)) {
			var foundTrue = false
			var foundFalse = false
			for (f <- filters) {
				if (f(log)) {
					foundTrue = true
				} else {
					foundFalse = true
				}
			}
			
			if ((foundTrue) || (foundFalse)) {
				filterMode match {
					case FilterMode.AllMatch => !foundFalse
					case FilterMode.AnyMatch => foundTrue
				}
			} else {
				true		// No filters
			}
		} else {
			false
		}
	}
}