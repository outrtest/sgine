package org.sgine.event

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import java.util.concurrent.atomic.AtomicInteger
import org.sgine.concurrent.{Executor, Time}

/**
 * 
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class EventSpec extends FlatSpec with ShouldMatchers {
  val sl = StringListenable
  val count = new AtomicInteger(0)
  val incrementListener: (String) => Unit = (s: String) => count.addAndGet(1)

  "StringListenable" should "have no listeners" in {
    validateAllEmpty()
  }

  it should "add a listener" in {
    sl.listeners.synchronous += incrementListener
  }

  it should "have synchronous listeners now" in {
    validateOneSynchronous()
  }

  it should "have one synchronous listener" in {
    sl.listeners.length should equal(1)
    sl.listeners.synchronous.length should equal(1)
    sl.listeners.asynchronous.length should equal(0)
    sl.listeners.concurrent.length should equal(0)
  }

  it should "receive a synchronous event" in {
    count.get should equal(0)
    sl.fire("Test 1")
    count.get should equal(1)
  }

  it should "remove the synchronous listener" in {
    sl.listeners.synchronous -= incrementListener
    validateAllEmpty()
  }

  it should "allow fall-through when no match" in {
    val l = sl.listeners.synchronous {
      case evt if (evt == "Not Matching") => // Will not happen
    }
    sl.fire("Test Fallthrough")
    validateOneSynchronous()
    sl.listeners.synchronous -= l
    validateAllEmpty()
  }

  it should "receive asynchronous events" in {
    count.set(0)
    val l = sl.listeners.asynchronous {
      case s => count.addAndGet(1)
    }
    validateOneAsynchronous()
    sl.fire("Test Asynchronous")
    Time.waitFor(1.0) {
      count.get() == 1
    } should equal(true)
    sl.listeners.asynchronous -= l
    validateAllEmpty()
  }

  it should "receive concurrent events" in {
    count.set(0)
    val l = sl.listeners.concurrent {
      case s => count.addAndGet(1)
    }
    validateOneConcurrent()
    sl.fire("Test Concurrent")
    Time.waitFor(1.0) {
      count.get() == 1
    } should equal(true)
    sl.listeners.concurrent -= l
    validateAllEmpty()
  }

  it should "wait for a specific event" in {
    Executor.invoke {
      Thread.sleep(100)
      sl.fire("specific event")
    }
    sl.listeners.synchronous.waitFor(1.0) {
      case event => "success"
    } should equal(Some("success"))
    validateAllEmpty()
  }

  it should "fail to match a specific event" in {
    Executor.invoke {
      Thread.sleep(100)
      sl.fire("specific event")
    }
    sl.listeners.synchronous.waitFor(0.5) {
      case event if (event == "non specific event") => "success"
    } should equal(None)
    validateAllEmpty()
  }

  it should "stop propagation of an event" in {
    
  }

  def validateOneSynchronous() = {
    sl.listeners.isEmpty should equal(false)
    sl.listeners.nonEmpty should equal(true)
    sl.listeners.synchronous.isEmpty should equal(false)
    sl.listeners.synchronous.nonEmpty should equal(true)
    sl.listeners.asynchronous.isEmpty should equal(true)
    sl.listeners.asynchronous.nonEmpty should equal(false)
    sl.listeners.concurrent.isEmpty should equal(true)
    sl.listeners.concurrent.nonEmpty should equal(false)
  }

  def validateOneAsynchronous() = {
    sl.listeners.isEmpty should equal(false)
    sl.listeners.nonEmpty should equal(true)
    sl.listeners.synchronous.isEmpty should equal(true)
    sl.listeners.synchronous.nonEmpty should equal(false)
    sl.listeners.asynchronous.isEmpty should equal(false)
    sl.listeners.asynchronous.nonEmpty should equal(true)
    sl.listeners.concurrent.isEmpty should equal(true)
    sl.listeners.concurrent.nonEmpty should equal(false)
  }

  def validateOneConcurrent() = {
    sl.listeners.isEmpty should equal(false)
    sl.listeners.nonEmpty should equal(true)
    sl.listeners.synchronous.isEmpty should equal(true)
    sl.listeners.synchronous.nonEmpty should equal(false)
    sl.listeners.asynchronous.isEmpty should equal(true)
    sl.listeners.asynchronous.nonEmpty should equal(false)
    sl.listeners.concurrent.isEmpty should equal(false)
    sl.listeners.concurrent.nonEmpty should equal(true)
  }

  def validateAllEmpty() = {
    sl.listeners.isEmpty should equal(true)
    sl.listeners.nonEmpty should equal(false)
    sl.listeners.synchronous.isEmpty should equal(true)
    sl.listeners.synchronous.nonEmpty should equal(false)
    sl.listeners.asynchronous.isEmpty should equal(true)
    sl.listeners.asynchronous.nonEmpty should equal(false)
    sl.listeners.concurrent.isEmpty should equal(true)
    sl.listeners.concurrent.nonEmpty should equal(false)
  }
}

object StringListenable extends Listenable[String]