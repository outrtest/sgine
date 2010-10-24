import java.io.File

import sbt._

class SgineProject(info: ProjectInfo) extends DefaultProject(info) {
	override def testSourceRoots = super.testSourceRoots +++ ("src" / "example")
	override def runClasspath = super.runClasspath +++ testClasspath +++ ("src" / "main" / "resources") +++ ("src" / "test" / "resources") +++ ("src" / "example" / "resources")

	lazy val exampleSourcePath: Path = ("src" / "example")
	def exampleResourcesPath = exampleSourcePath / resourcesDirectoryName
	def exampleResources = descendents(exampleResourcesPath ##, "*")
	
	override def packageTestPaths = super.packageTestPaths +++ exampleResources
	
	lazy val runExample = task {
		args => runTask(Some(args(0)), runClasspath).dependsOn(testCompile)
	} completeWith(mainSources.getRelativePaths.toSeq.map(_.replace("/", ".").replace(".scala", "")))
	
	override def fork = Some(new ForkScalaRun {
		val (os, separator) = System.getProperty("os.name").split(" ")(0).toLowerCase match {
            case "linux" => "linux" -> ":"
            case "mac" => "macosx" -> ":"
            case "windows" => "windows" -> ";"
            case "sunos" => "solaris" -> ":"
            case x => x -> ":"
        }
        
        override def runJVMOptions = super.runJVMOptions ++ Seq("-Djava.library.path=" + System.getProperty("java.library.path") + separator + ("lib" / "native" / os))
        
        override def scalaJars = Seq(buildLibraryJar.asFile, buildCompilerJar.asFile)
	})
}