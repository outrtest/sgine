package org.sgine.log

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.log._

class LogSpec extends FlatSpec with ShouldMatchers {
	private var infoCount = 0
	
	object TestLogger extends Logger {
		def log(log: Log) = {
			if (log.level == LogLevel.Info) {
				infoCount += 1
			}
		}
	}
	
	Log.addLogger(TestLogger)
	
	"Log" should "have nothing logged in info" in {
		infoCount should equal(0)
	}
	
	it should "have one item logged in info after call to info" in {
		Log("testing")
		infoCount should equal(1)
	}
	
	it should "have one item logged in info after call to warn" in {
		Log("testing", level = LogLevel.Warning)
		infoCount should equal(1)
	}
}