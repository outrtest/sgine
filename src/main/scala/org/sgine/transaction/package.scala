package org.sgine

package object transaction {
	def transaction(f: => Unit) = Transaction(f)
}