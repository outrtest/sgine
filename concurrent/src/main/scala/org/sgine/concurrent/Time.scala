/*
 * Copyright (c) 2011 Sgine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 *  Neither the name of 'Sgine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sgine.concurrent


import scala.math._
import org.sgine.{Enumerated, EnumEntry, Precision}
import java.util.Calendar

/**
 * Time represents convenience values and utilities
 * for lengths of time. All values are represented
 * as Doubles of time in seconds.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed case class Time(value: Double, formatter: Calendar => String) extends EnumEntry[Time] {
  def this(value: Double, format: String) = this(value, calendar => format.format(calendar))
}

object Time extends Enumerated[Time] {
  val Second = new Time(1.0, "%1$tD %1$tT")
  val Minute = new Time(60.0 * Second.value, "%1$tD %1$tR")
  val Hour = new Time(60.0 * Minute.value, "%1$tD %1$tH")
  val Day = new Time(24.0 * Hour.value, "%1$tD")
  val Week = new Time(7.0 * Day.value, calendar => "%1$tY %2$s".format(calendar, calendar.get(Calendar.WEEK_OF_YEAR)))
  val Month = new Time(30.0 * Day.value, "%1$tB %1$tY")
  val Year = new Time(12.0 * Month.value, "%1$tY")

  /**
   * Invokes the wrapped function and returns the time in seconds it took to complete as a Double.
   */
  def elapsed(f: => Any): Double = {
    val time = System.nanoTime
    f
    (System.nanoTime - time) / Precision.Nanoseconds.conversion
  }

  /**
   * Converts time in milliseconds to a short String representation.
   */
  def elapsed(time: Long): String = elapsed(time.toDouble / 1000.0)

  /**
   * Converts time in seconds to a short String representation.
   */
  def elapsed(time: Double): String = {
    val format = "%,.2f"
    var value: Double = time
    var ending = "ms"
    if (time > Year.value) {
      value = time / Year.value
      ending = " years"
    } else if (time > Month.value) {
      value = time / Month.value
      ending = " months"
    } else if (time > Week.value) {
      value = time / Week.value
      ending = " weeks"
    } else if (time > Day.value) {
      value = time / Day.value
      ending = " days"
    } else if (time > Hour.value) {
      value = time / Hour.value
      ending = " hours"
    } else if (time > Minute.value) {
      value = time / Minute.value
      ending = " minutes"
    } else if (time > Second.value) {
      value = time / Second.value
      ending = " seconds"
    }

    String.format(format + ending, value.asInstanceOf[AnyRef])
  }

  /**
   * Converts time in milliseconds to a long String representation.
   */
  def elapsedExact(time: Long) = {
    val b = new StringBuilder()

    var elapsed: Double = time
    var years, months, weeks, days, hours, minutes, seconds = 0

    while (elapsed >= Year.value) {
      years += 1
      elapsed -= Year.value
    }
    while (elapsed >= Month.value) {
      months += 1
      elapsed -= Month.value
    }
    while (elapsed >= Week.value) {
      weeks += 1
      elapsed -= Week.value
    }
    while (elapsed >= Day.value) {
      days += 1
      elapsed -= Day.value
    }
    while (elapsed >= Hour.value) {
      hours += 1
      elapsed -= Hour.value
    }
    while (elapsed >= Minute.value) {
      minutes += 1
      elapsed -= Minute.value
    }
    while (elapsed >= Second.value) {
      seconds += 1
      elapsed -= Second.value
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

  /**
   * Convenience method to sleep a specific amount of time in seconds.
   */
  def sleep(time: Double) = Thread.sleep(millis(time))

  /**
   * Converts time in seconds to milliseconds.
   */
  def millis(time: Double) = round((time * 1000.0).toFloat)

  /**
   * Waits for <code>condition</code> to return true. This method will wait
   * <code>time</code> (in seconds) for the condition and will return false
   * if the condition is not met within that time. Further, a negative value
   * for <code>time</code> will cause the wait to occur until the condition
   * is true.
   *
   * @param time
   *              The time to wait for the condition to return true.
   * @param precision
   *              The recycle period between checks. Defaults to 0.01s.
   * @param start
   *              The start time in milliseconds since epoc. Defaults to
   *              System.currentTimeMillis.
   * @param errorOnTimeout
   *              If true, throws a java.util.concurrent.TimeoutException upon
   *              timeout. Defaults to false.
   * @param condition
   *              The functional condition that must return true.
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