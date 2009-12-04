package org.sgine.math

import org.sgine.util.PerformanceProfiler
import java.nio.{DoubleBuffer, FloatBuffer}

/**
 * Times various math operations and outputs results.
 *
 * This isn't a part of the normal test framework as it doesn't produce clear quantifiable results,
 * but it is useful for monitoring the effect of optimizations and changes.
 */
// TODO: Some performance measurement framework and reporting tool that plugs into hudson and shows development of performance accross build numbers.
// It could maybe also be run on different machines and utilize the graphics card, and can produce timing information that can be uploaded / compared.
object MathPerformanceTest {

  def main(args: Array[String]) {

    val profiler = new PerformanceProfiler[(DoubleBuffer,DoubleBuffer)]( "DoubleBuffer batch transform" )
    profiler.warmupRounds = 1000
    profiler.testRounds = 1000

    val testDataSize: Int = 10000

/*
    The results of partially unrolled vs non-unrolled transformations with 10 000 vectors were:
    
    Performance measure of DoubleBuffer batch transform
      Number of tests: 2
      Test results:
        Unrolled Matrix conversion
          Numer of rounds: 1000
          Average duration: 0.8806669380000003 ms


        Normal Matrix conversion
          Numer of rounds: 1000
          Average duration: 0.8395143179999996 ms

    Based on these results the partially unrolled transform method was removed.
*/
/*
    profiler.addTest( "Unrolled Matrix conversion",  _ => createTestData(testDataSize))
    { case (in : DoubleBuffer, out : DoubleBuffer) =>
      val m = Matrix4()
      m.transformPartiallyUnrolled(in, out)
    }
*/

    profiler.addTest( "Normal Matrix conversion" , _ =>createTestData(testDataSize))
    { case (in : DoubleBuffer, out : DoubleBuffer) =>
      val m = Matrix4()
      m.transform(in, out)
    }

    profiler.runTestsAndPrintResults
  }


  def createTestData(size : Int) : (DoubleBuffer, DoubleBuffer) = {
    val buffer: DoubleBuffer = DoubleBuffer.allocate(size * 3)
    0 until size foreach { i =>
      buffer.put( Math.random * 100.0 )
    }

    buffer.clear

    (buffer, DoubleBuffer.allocate(size * 3))
  }

}
