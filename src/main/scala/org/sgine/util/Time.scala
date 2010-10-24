package org.sgine.util

import java.util.Calendar

import scala.math._

/**
 * Time represents convenience values and utilities
 * for lengths of time. All values are represented
 * as Doubles of time in seconds.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Time {
	val Second = 1.0
	val Minute = 60.0 * Second
	val Hour = 60.0 * Minute
	val Day = 24.0 * Hour
	val Week = 7.0 * Day
	val Month = 30.0 * Day
	val Year = 12.0 * Month
	
	def elapsed(time: Long): String = elapsed(time.toDouble / 1000.0)
	
	def elapsed(time: Double): String = {
		val format = "%,.2f"
		var value: Double = time
		var ending = "ms"
		if (time > Year) {
			value = time / Year
			ending = " years"
		} else if (time > Month) {
			value = time / Month
			ending = " months"
		} else if (time > Week) {
			value = time / Week
			ending = " weeks"
		} else if (time > Day) {
			value = time / Day
			ending = " days"
		} else if (time > Hour) {
			value = time / Hour
			ending = " hours"
		} else if (time > Minute) {
			value = time / Minute
			ending = " minutes"
		} else if (time > Second) {
			value = time / Second
			ending = " seconds"
		}
		
		String.format(format + ending, value.asInstanceOf[AnyRef])
	}
	
	def elapsedExact(time: Long) = {
		val b = new StringBuilder()
		
		var elapsed: Double = time
		var years, months, weeks, days, hours, minutes, seconds = 0
		
		while (elapsed >= Year) {
			years += 1
			elapsed -= Year
		}
		while (elapsed >= Month) {
			months += 1
			elapsed -= Month
		}
		while (elapsed >= Week) {
			weeks += 1
			elapsed -= Week
		}
		while (elapsed >= Day) {
			days += 1
			elapsed -= Day
		}
		while (elapsed >= Hour) {
			hours += 1
			elapsed -= Hour
		}
		while (elapsed >= Minute) {
			minutes += 1
			elapsed -= Minute
		}
		while (elapsed >= Second) {
			seconds += 1
			elapsed -= Second
		}

		if (years > 0) b.append(years + "y, ")
		if (months > 0) b.append(months + "m, ")
		if (weeks > 0) b.append(weeks + "w, ")
		if (days > 0) b.append(days + "d, ")
		if (hours > 0) b.append(hours + "h, ")
		if (minutes > 0) b.append(minutes + "m, ")
		if (seconds > 0) b.append(seconds + "s, ")
		b.append(elapsed + "ms")
		
		b.toString()
	}
	
	def futureCalendar(time: Double, c: Calendar = Calendar.getInstance) = {
		c.add(Calendar.MILLISECOND, millis(time))
		c
	}
	
	def sleep(time: Double) = Thread.sleep(millis(time))
	
	def millis(time: Double) = round((time * 1000.0).toFloat)
	
	/**
	 * Waits for <code>condition</code> to return true. This method will wait
	 * <code>time</code> (in seconds) for the condition and will return false
	 * if the condition is not met within that time. Further, a negative value
	 * for <code>time</code> will cause the wait to occur until the condition
	 * is true.
	 * 
	 * @param time
	 * 		The time to wait for the condition to return true.
	 * @param precision
	 * 		The recycle period between checks. Defaults to 0.01s.
	 * @param start
	 * 		The start time in milliseconds since epoc. Defaults to
	 * 		System.currentTimeMillis.
	 * @param errorOnTimeout
	 * 		If true, throws a java.util.concurrent.TimeoutException upon
	 * 		timeout. Defaults to false.
	 * @param condition
	 * 		The functional condition that must return true.
	 */
	@scala.annotation.tailrec
	def waitFor(time: Double, precision: Double = 0.01, start: Long = System.currentTimeMillis, errorOnTimeout: Boolean = false)(condition: => Boolean): Boolean = {
		val p = round(precision * 1000.0)
		if (!condition) {
			if ((time >= 0.0) && (System.currentTimeMillis - start > millis(time))) {
				if (errorOnTimeout) throw new java.util.concurrent.TimeoutException()
				false
			} else {
				Thread.sleep(p)
				
				waitFor(time, precision, start)(condition)
			}
		} else {
			true
		}
	}
}