package org.sgine.util

import java.net._

object Desktop {
	private lazy val d = java.awt.Desktop.getDesktop()
	
	def browse(uri: URI) = {
		d.browse(uri)
	}
	
	def main(args: Array[String]) = {
		if (args.length == 2) {
			if (args(0) == "browse") {
				val uri = args(1).replaceAll("[\\\\]", "/")
				browse(new URI(uri))
			}
		}
	}
}