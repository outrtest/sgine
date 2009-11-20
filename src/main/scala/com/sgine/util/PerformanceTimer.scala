package com.sgine.util

/**
 * A timer for measuring the duration of some code block.
 */
class PerformanceTimer {

  /**
   * The time in nanoseconds for the
   */
  def time( block : Unit => Unit ) {

    System.nanoTime
    
  }

}
