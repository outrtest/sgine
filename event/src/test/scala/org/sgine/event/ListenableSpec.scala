package org.sgine.event

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.sgine.concurrent.{Executor, AtomicInt, Time}
import org.sgine.{Parent, Child}
import org.sgine.bus.{RoutingResults, Routing, Bus}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 12/3/11
 */
class ListenableSpec extends WordSpec with ShouldMatchers {
  "Listenable" when {
    "one synchronous listener is added" should {
      var received = false
      var listener: Listener = null
      "add a listener" in {
        listener = TestListenable.listeners.synchronous {
          case event => received = true
        }
      }
      "have one listener" in {
        TestListenable.listeners.synchronous.length should equal(1)
      }
      "fire an event" in {
        TestListenable.fire(Event(TestListenable))
      }
      "have received the event on the listener" in {
        received should equal(true)
      }
      "remove the listener" in {
        TestListenable.listeners.synchronous -= listener
      }
      "have no listeners" in {
        TestListenable.listeners.synchronous.isEmpty should equal(true)
      }
      "have no nodes on the Bus" in {
        Bus.isEmpty should equal(true)
      }
    }
    "one asynchronous listener is added" should {
      var received = false
      var listener: Listener = null
      "add a listener" in {
        listener = TestListenable.listeners.asynchronous {
          case event => {
            Thread.sleep(500)
            received = true
          }
        }
      }
      "have one listener" in {
        TestListenable.listeners.asynchronous.length should equal(1)
      }
      "fire an event" in {
        TestListenable.fire(Event(TestListenable))
      }
      "not have received the event on the listener" in {
        received should equal(false)
      }
      "receive the event a little later" in {
        Time.waitFor(1.0) {
          received
        } should equal(true)
      }
      "remove the listener" in {
        TestListenable.listeners.asynchronous -= listener
      }
      "have no listeners" in {
        TestListenable.listeners.asynchronous.isEmpty should equal(true)
      }
      "have no nodes on the Bus" in {
        Bus.isEmpty should equal(true)
      }
    }
    "one concurrent listener is added" should {
      var received = false
      var listener: Listener = null
      "add a listener" in {
        listener = TestListenable.listeners.concurrent {
          case event => {
            Thread.sleep(500)
            received = true
          }
        }
      }
      "have one listener" in {
        TestListenable.listeners.concurrent.length should equal(1)
      }
      "fire an event" in {
        TestListenable.fire(Event(TestListenable))
      }
      "not have received the event on the listener" in {
        received should equal(false)
      }
      "receive the event a little later" in {
        Time.waitFor(1.0) {
          received
        } should equal(true)
      }
      "remove the listener" in {
        TestListenable.listeners.concurrent -= listener
      }
      "have no listeners" in {
        TestListenable.listeners.concurrent.isEmpty should equal(true)
      }
      "have no nodes on the Bus" in {
        Bus.isEmpty should equal(true)
      }
    }
    "utilizing the 'once' convenience method" should {
      val received = new AtomicInt(0)
      var listener: Listener = null
      "add a listener" in {
        listener = TestListenable.listeners.synchronous.once[Event] {
          case event => {
            received += 1
          }
        }
      }
      "have one listener" in {
        TestListenable.listeners.synchronous.length should equal(1)
      }
      "fire an event" in {
        TestListenable.fire(Event(TestListenable))
      }
      "have received one message" in {
        received() should equal(1)
      }
      "fire another event" in {
        TestListenable.fire(Event(TestListenable))
      }
      "not have received any additional messages on the original listener" in {
        received() should equal(1)
      }
      "have no listeners attached" in {
        TestListenable.listeners.synchronous.isEmpty should equal(true)
      }
      "have no nodes on the Bus" in {
        Bus.isEmpty should equal(true)
      }
    }
    "utilizing the 'waitFor' convenience method" should {
      val received = new AtomicInt(0)
      "concurrently wait for an event" in {
        Executor.invoke {
          TestListenable.listeners.synchronous.waitFor[Event, Unit](5.0) {
            case event => received += 1
          }
        }
        Time.waitFor(2.0) {
          TestListenable.listeners.synchronous.nonEmpty
        }
      }
      "fire an event" in {
        TestListenable.fire(Event(TestListenable))
      }
      "should have received an event" in {
        received() should equal(1)
      }
      "fire another event" in {
        TestListenable.fire(Event(TestListenable))
      }
      "should not have received another event" in {
        received() should equal(1)
      }
      "have no listeners attached" in {
        TestListenable.listeners.synchronous.isEmpty should equal(true)
      }
      "have no nodes on the Bus" in {
        Bus.isEmpty should equal(true)
      }
    }
    "listening for an event on an ancestor" should {
      var received = false
      var listener: Listener = null
      "add an ancestor listener to the child" in {
        listener = TestChildListenable.listeners.synchronous.ancestor {
          case event => received = true
        }
      }
      "fire an event on the child" in {
        TestChildListenable.fire(Event(TestChildListenable))
      }
      "not have received an event on the child" in {
        received should equal(false)
      }
      "fire an event on the parent" in {
        TestParentListenable.fire(Event(TestParentListenable))
      }
      "have received an event on the child" in {
        received should equal(true)
      }
      "remove the listener" in {
        TestChildListenable.listeners.synchronous -= listener
      }
      "have no nodes on the Bus" in {
        Bus.isEmpty should equal(true)
      }
    }
    "listening for an event on an parent" should {
      var received = false
      var listener: Listener = null
      "add an ancestor listener to the child" in {
        listener = TestChildListenable.listeners.synchronous.parent {
          case event => received = true
        }
      }
      "fire an event on the child" in {
        TestChildListenable.fire(Event(TestChildListenable))
      }
      "not have received an event on the child" in {
        received should equal(false)
      }
      "fire an event on the parent" in {
        TestParentListenable.fire(Event(TestParentListenable))
      }
      "have received an event on the child" in {
        received should equal(true)
      }
      "remove the listener" in {
        TestChildListenable.listeners.synchronous -= listener
      }
      "have no nodes on the Bus" in {
        Bus.isEmpty should equal(true)
      }
    }
    "listening for an event on a descendant" should {
      var received = false
      var listener: Listener = null
      "add a descendant listener to the parent" in {
        listener = TestParentListenable.listeners.synchronous.descendant {
          case event => received = true
        }
      }
      "fire an event on the parent" in {
        TestParentListenable.fire(Event(TestParentListenable))
      }
      "not receive an event on the child" in {
        received should equal(false)
      }
      "fire an event on the child" in {
        TestChildListenable.fire(Event(TestChildListenable))
      }
      "have received an event on the parent" in {
        received should equal(true)
      }
      "remove the listener" in {
        TestChildListenable.listeners.synchronous -= listener
      }
      "have no nodes on the Bus" in {
        Bus.isEmpty should equal(true)
      }
    }
    "listening for an event on a child" should {
      var received = false
      var listener: Listener = null
      "add a descendant listener to the parent" in {
        listener = TestParentListenable.listeners.synchronous.child {
          case event => received = true
        }
      }
      "fire an event on the parent" in {
        TestParentListenable.fire(Event(TestParentListenable))
      }
      "not receive an event on the child" in {
        received should equal(false)
      }
      "fire an event on the child" in {
        TestChildListenable.fire(Event(TestChildListenable))
      }
      "have received an event on the parent" in {
        received should equal(true)
      }
      "remove the listener" in {
        TestChildListenable.listeners.synchronous -= listener
      }
      "have no nodes on the Bus" in {
        Bus.isEmpty should equal(true)
      }
    }
    "listener stopping processing via Routing.Stop" should {
      var received1 = false
      var received2 = false
      var listener1: Listener = null
      var listener2: Listener = null
      "add the first listener" in {
        listener1 = TestListenable.listeners.synchronous {
          case evt => {
            received1 = true
            Routing.Stop
          }
        }
      }
      "add the second listener" in {
        listener2 = TestListenable.listeners.synchronous {
          case evt => {
            received2 = true
          }
        }
      }
      "fire an event" in {
        TestListenable.fire(Event(TestListenable)) should equal(Routing.Stop)
      }
      "message should have been received on first listener" in {
        received1 should equal(true)
      }
      "message should not have been received on second listener" in {
        received2 should equal(false)
      }
      "remove the listeners" in {
        TestListenable.listeners.synchronous -= listener1
        TestListenable.listeners.synchronous -= listener2
      }
      "have no nodes on the Bus" in {
        Bus.isEmpty should equal(true)
      }
    }
    "listener propagating Routing.Results" should {
      var listener1: Listener = null
      var listener2: Listener = null
      var listener3: Listener = null
      var routing: Routing = null
      "add the first listener" in {
        listener1 = TestListenable.listeners.synchronous {
          case evt => Routing.Results(List(1))
        }
      }
      "add the second listener" in {
        listener2 = TestListenable.listeners.synchronous {
          case evt => Routing.Results(List(2))
        }
      }
      "add the third listener" in {
        listener3 = TestListenable.listeners.synchronous {
          case evt => Routing.Results(List(3))
        }
      }
      "fire an event" in {
        routing = TestListenable.fire(Event(TestListenable))
      }
      "message should be a Routing.Results of List(1, 2, 3)" in {
        routing.name should equal("Results")
        routing.asInstanceOf[RoutingResults].results should equal(List(1, 2, 3))
      }
      "remove the listeners" in {
        TestListenable.listeners.synchronous -= listener1
        TestListenable.listeners.synchronous -= listener2
        TestListenable.listeners.synchronous -= listener3
      }
      "have no nodes on the Bus" in {
        Bus.isEmpty should equal(true)
      }
    }
  }
}

object TestListenable extends Listenable

object TestParentListenable extends Listenable with Parent[Listenable] {
  lazy val children = List(TestChildListenable)
}

object TestChildListenable extends Listenable with Child[Listenable] {
  val parent = () => TestParentListenable
}