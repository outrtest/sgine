package com.sgine.math

import com.sgine.util.PerformanceProfiler
import java.nio.{DoubleBuffer, FloatBuffer}

/**
 * Times various math operations and outputs results.
 *
 * This isn't a part of the normal test framework as it doesn't produce clear quantifiable results,
 * but it is useful for monitoring the effect of optimizations and changes.
 */
// TODO: Some performance measuremtn framework and reporting tool that plugs into hudson and shows development of
// performance accross build numbers.
// It could maybe also be run on different machines and utilize the graphics card, and can produce timing information that can be uploaded / compared.
object MathPerformanceTest {

  def main(args: Array[String]) {

    val profiler = new PerformanceProfiler( "DoubleBuffer batch transform" )

/* TODO: FIX
    profiler.addTest[(DoubleBuffer,DoubleBuffer)]( "Unrolled Matrix conversion",  _ => createTestData(100))
    { case (in : DoubleBuffer, out : DoubleBuffer) =>
      val m = Matrix4()
      m.transform(in, out)
    }

    profiler.addTest[(DoubleBuffer,DoubleBuffer)]( "Normal Matrix conversion" , _ =>createTestData(100))
    { case (in : DoubleBuffer, out : DoubleBuffer) =>
      val m = Matrix4()
      m.transformNonUnrolled(in, out)
    }
*/

    profiler.runTestsAndPrintResults
  }


  def createTestData(size : Int) : (DoubleBuffer, DoubleBuffer) = {
    val buffer: DoubleBuffer = DoubleBuffer.allocate(size * 3)
    0 until size foreach { i =>
      buffer.put( Math.random * 100.0 )
    }

    (buffer, DoubleBuffer.allocate(size * 3))
  }
}
