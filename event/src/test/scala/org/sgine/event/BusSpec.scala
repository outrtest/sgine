package org.sgine.event

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import java.util.concurrent.atomic.AtomicInteger

/**
 * Validation of logic in Bus
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class BusSpec extends FlatSpec with ShouldMatchers {
  val test = new Test()
  val count = new AtomicInteger(0)

  "StringEventSupport" should "invoke messages on the Bus" in {
    reset()
    Bus.listeners.synchronous[UberEvent] {
      case event => count.addAndGet(1)
    }
    test.uber.fire(new UberEvent)
    count.get should equal(1)
  }

  it should "invoke messages on the Bus when listening to Event" in {
    reset()
    Bus.listeners.synchronous[Event] {
      case event => count.addAndGet(1)
    }
    test.uber.fire(new UberEvent)
    count.get should equal(1)
  }

  def reset() = {
    test.uber.clear()
    test.children = Nil
    count.set(0)
    Bus.listeners.clearAll()
  }
}