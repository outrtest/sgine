package com.sgine.util

/**
 * Start of a framework for measuring perfromance and reporting the results.
 */
// TODO: Some tool for iterating through classes in source dirs and running the ones inheriting the performance test trait.
// TODO: Refactor this to be unit test like instead with an overrideable setup method and reflection found test methods, this current approach seems clumsy to use
final case class PerformanceProfiler[T]( subject : String ) {

  var warmupRounds = 5
  var testRounds = 10

  private var tests : List[(String,Unit=>T,T=>Unit)] = Nil

  def addTest( testName : String, setup: Unit => T) (performanceTest : T => Unit ) {
    tests = tests ::: List( (testName, setup.asInstanceOf[Unit => T], performanceTest.asInstanceOf[T => Unit]) )
  }

  def runTestsAndPrintResults() {
    println( "Running "+tests.size+" performance test"+(if(tests.size!=1) "s" else "")+" for " + subject + "\n ..." )
    println( runTests )
  }

  def runTests() : String = {
    makeSummaryReport( subject,
      tests map { case (testName : String, setup : (Unit=>T), test : (T=>Unit)) =>

        // Warm up the JVM -- that is, allow the Just In Time compiler to optimize the code for the tested block.
        runTest(warmupRounds, setup, test)

        // Do the actual test runs and construct test report
        makeTestReport( testName, runTest(testRounds, setup, test))
      } )
  }

  /**
   * Gets a list of test run durations in seconds, and creates a report.
   */
  def makeTestReport( testName : String, durations : Seq[Double]) : String = {
    "    " + testName + "\n"+
    "      Numer of rounds: " + durations.size + "\n" +
    "      Average duration: " + 1000.0 * (durations.sum / durations.size) + " ms\n" +
/*
    "      Round durations:\n" +
    "        " + durations.mkString( " s\n        " ) + (if (durations.size>0) "s" else "") +
*/
    "\n"
  }

  /**
   * Gets a list of test run durations in seconds, and creates a report.
   */
  def makeSummaryReport( subject : String, testReports : Seq[String]) : String = {
    "Performance measure of " + subject + "\n" +
    "  Number of tests: " + testReports.size + "\n"+
    "  Test results:\n" + testReports.mkString( "\n" ) + "\n"
  }

  /**
   *  Returns the durations of each test run in seconds.
   */
  def runTest( numberOfRuns : Int,  setup : (Unit=>T), test : (T=>Unit) ) : Seq[Double] = {
    1 to numberOfRuns map { i =>

      val testData = setup()

      val startTime_ns = System.nanoTime

      test( testData )

      val endTime_ns = System.nanoTime

      // Convert to seconds from nanoseconds:
      (1.0*(endTime_ns - startTime_ns) / 1.0e9).toDouble
    }
  }

}
