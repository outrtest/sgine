import sbt._
import Keys._

object SgineBuild extends Build {
  val lwjglVersion = "2.8.1"

  val baseSettings = Defaults.defaultSettings ++ Seq(
    version := "1.0-SNAPSHOT",
    organization := "org.sgine",
    scalaVersion := "2.9.1",
    libraryDependencies ++= Seq(
      "org.scalatest" % "scalatest_2.9.1" % "1.6.1" % "test"
    ),
    publishTo <<= (version) {
      version: String =>
        val nexus = "http://nexus.scala-tools.org/content/repositories/"
        if (version.trim.endsWith("SNAPSHOT")) {
          Some("snapshots" at nexus + "snapshots/")
        }
        else {
          Some("releases" at nexus + "releases/")
        }
    },
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    publishArtifact in Test := true,
    scalacOptions ++= Seq("-unchecked", "-deprecation")
  )

  private def createSettings(_name: String) = baseSettings ++ Seq(name := _name)

  lazy val root = Project("root", file("."), settings = createSettings("sgine-root"))
      .settings(publishArtifact in Compile := false, publishArtifact in Test := false,
    unmanagedSourceDirectories in Compile <<= baseDirectory apply {
      dir =>
        Seq(dir / "concurrent" / "src" / "main" / "scala",
          dir / "core" / "src" / "main" / "scala",
          dir / "event" / "src" / "main" / "scala",
          dir / "input" / "src" / "main" / "scala",
          dir / "property" / "src" / "main" / "scala",
          dir / "reflect" / "src" / "main" / "scala",
          dir / "scene" / "src" / "main" / "scala",
          dir / "ui" / "src" / "main" / "scala"
        )
    })
      .aggregate(concurrent, core, event, input, property, reflect, scene, ui)
  lazy val concurrent = Project("concurrent", file("concurrent"),
    settings = createSettings("sgine-concurrent"))
      .dependsOn(core, reflect)
  lazy val core = Project("core", file("core"), settings = createSettings("sgine-core"))
      .dependsOn(reflect)
  lazy val event = Project("event", file("event"), settings = createSettings("sgine-event"))
      .dependsOn(concurrent, core)
  lazy val input = Project("input", file("input"), settings = createSettings("sgine-input"))
      .dependsOn(core, event, property)
  lazy val property = Project("property", file("property"),
    settings = createSettings("sgine-property"))
      .dependsOn(event, scene)
  lazy val reflect = Project("reflect", file("reflect"), settings = createSettings("sgine-reflect"))
      .settings(libraryDependencies += "com.thoughtworks.paranamer" % "paranamer" % "2.4")
  lazy val scene = Project("scene", file("scene"), settings = createSettings("sgine-scene"))
      .dependsOn(event)
  lazy val ui = Project("ui", file("ui"), settings = createSettings("sgine-ui"))
      .dependsOn(core, event, input, property, scene)
}