package com.sgine.math

import java.nio.DoubleBuffer
import com.sgine.util.PerformanceProfiler

/**
 * Test performance of fixed point math
 */
@Deprecated
object FixedPerformanceTest {

  val fixed1: Fixed = Fixed.make(42.12)
  val fixed2: Fixed = Fixed.make(532.123)
  val mfixed1: Long = fixed1.rawValue
  val mfixed2: Long = fixed2.rawValue
  val double1: Double = 42.12
  val double2: Double = 532.123
  val float1: Float = 42.12f
  val float2: Float = 532.123f

  def main(args: Array[String]) {

    val profiler = new PerformanceProfiler[Any]( "Fixed point math timing" )
    profiler.warmupRounds = 300
    profiler.testRounds = 300

    val testDataSize: Int = 10000

    profiler.addTest( "Float math" , _ =>createFloatTestData(testDataSize))
    { in =>
      val data = in.asInstanceOf[Array[Float]]
      var i = 0
      while (i < data.length) {
        val result = (data( i ) * float1) + float2
        data( i ) = result
        i = i + 1
      }
    }

    profiler.addTest( "Double math" , _ =>createDoubleTestData(testDataSize))
    { in =>
      val data = in.asInstanceOf[Array[Double]]
      var i = 0
      while (i < data.length) {
        val result = (data( i ) * double1) + double2
        data( i ) = result
        i = i + 1
      }
    }

    profiler.addTest( "Fixed math object" , _ =>createFixedTestData(testDataSize))
    { in =>
      val data = in.asInstanceOf[Array[Fixed]]
      var i = 0
      while (i < data.length) {
        val result = (data( i ) * fixed1) + fixed2
        data( i ) = result
        i = i + 1
      }
    }

    profiler.addTest( "Fixed math manually inlined" , _ =>createManualFixedTestData(testDataSize))
    { in =>
      val data = in.asInstanceOf[Array[Long]]
      var i = 0
      while (i < data.length) {
        val result = ((data( i ) * mfixed1) >> 16) + mfixed2
        data( i ) = result
        i = i + 1
      }
    }

    profiler.runTestsAndPrintResults
  }


  def createFloatTestData(size : Int) : Array[Float] = {
    val data = new Array[Float]( size )

    0 until size foreach { i =>
      data( i ) =  (Math.random * 1000.0).toFloat
    }

    data
  }

  def createDoubleTestData(size : Int) : Array[Double] = {
    val data = new Array[Double]( size )

    0 until size foreach { i =>
      data( i ) =  Math.random * 1000.0
    }

    data
  }

  def createFixedTestData(size : Int) : Array[Fixed] = {
    val data = new Array[Fixed]( size )

    0 until size foreach { i =>
      data( i ) = Fixed.make( Math.random * 1000.0 )
    }

    data
  }

  def createManualFixedTestData(size : Int) : Array[Long] = {
    val data = new Array[Long]( size )

    0 until size foreach { i =>
      data( i ) = Fixed.make( Math.random * 1000.0 ).rawValue
    }

    data
  }


}
