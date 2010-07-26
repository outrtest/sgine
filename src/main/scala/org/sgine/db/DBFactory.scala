package org.sgine.db

import java.io.File

trait DBFactory {
	def apply(file: File, autoCommit: Boolean): DB
}