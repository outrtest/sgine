import sbt._
import Keys._

import Dependencies._

object SgineBuild extends Build {
  val baseSettings = Defaults.defaultSettings ++ Seq(
    version := "0.1",
    organization := "org.sgine",
    scalaVersion := "2.9.2",
    libraryDependencies ++= Seq(
      scalaTest,
      powerScalaCore,
      powerScalaEvent,
      powerScalaProperty,
      powerScalaHierarchy
    ),
    publishTo <<= version {
      (v: String) =>
        val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT"))
          Some("snapshots" at nexus + "content/repositories/snapshots")
        else
          Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    publishArtifact in Test := false,
    pomExtra := (
      <url>http://sgine.org</url>
        <licenses>
          <license>
            <name>BSD-style</name>
            <url>http://www.opensource.org/licenses/bsd-license.php</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <developerConnection>scm:https://sgine.googlecode.com/hg</developerConnection>
          <connection>scm:http://sgine.googlecode.com/hg</connection>
          <url>http://code.google.com/p/sgine/source/browse/</url>
        </scm>
        <developers>
          <developer>
            <id>darkfrog</id>
            <name>Matt Hicks</name>
            <url>http://matthicks.com</url>
          </developer>
        </developers>),
    scalacOptions ++= Seq("-unchecked", "-deprecation")
//    platformName in Android := "android-7"
  )

  private def createSettings(_name: String) = baseSettings ++ Seq(name := _name)

  lazy val root = Project("root", file("."), settings = createSettings("sgine-root"))
    .settings(publishArtifact in Compile := false, publishArtifact in Test := false,
    unmanagedSourceDirectories in Compile <<= baseDirectory apply {
      dir =>
        Seq(dir / "input" / "src" / "main" / "scala",
          dir / "media" / "src" / "main" / "scala",
          dir / "ui" / "src" / "main" / "scala"
        )
    })
    .aggregate(input, media, ui)
  lazy val input = Project("input", file("input"), settings = createSettings("sgine-input"))
  lazy val media = Project("media", file("media"), settings = createSettings("sgine-media"))
    .dependsOn(ui)
  lazy val ui = Project("ui", file("ui"), settings = createSettings("sgine-ui"))
    .settings(publishArtifact in Test := true)
    .dependsOn(input)
}

object Dependencies {
  val paranamer = "com.thoughtworks.paranamer" % "paranamer" % "2.4"
  val scalaTest = "org.scalatest" % "scalatest_2.9.1" % "1.7.1" % "test"

  val powerScalaVersion = "1.0"
  val powerScalaCore = "org.powerscala" %% "powerscala-core" % powerScalaVersion
  val powerScalaEvent = "org.powerscala" %% "powerscala-event" % powerScalaVersion
  val powerScalaHierarchy = "org.powerscala" %% "powerscala-hierarchy" % powerScalaVersion
  val powerScalaProperty = "org.powerscala" %% "powerscala-property" % powerScalaVersion
}