import sbt._
import Keys._

object SgineBuild extends Build {
  val lwjglVersion = "2.7.1"

  val baseSettings = Defaults.defaultSettings ++ Seq(
    version := "1.0-SNAPSHOT",
    organization := "org.sgine",
    scalaVersion := "2.9.0-1",
    libraryDependencies ++= Seq(
      "org.scalatest" % "scalatest_2.9.0" % "1.6.1" % "test"
    ),
    publishTo <<= (version) { version: String =>
      val nexus = "http://nexus.scala-tools.org/content/repositories/"
      if (version.trim.endsWith("SNAPSHOT")) {
        Some("snapshots" at nexus + "snapshots/")
      } else {
        Some("releases" at nexus + "releases/")
      }
    },
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    publishArtifact in Test := true,
    resolvers += "LWJGL Repository" at "http://adterrasperaspera.com/lwjgl/"
  )
  private def createSettings(_name: String) = baseSettings ++ Seq(name := _name)

  lazy val root = Project("root", file("."), settings = createSettings("sgine-root"))
    .settings(publishArtifact in Compile := false, publishArtifact in Test := false)
    .aggregate(concurrent, core, event, math, openglGenerator, openglApi, openglLwjgl, openglAndroid, openglNehe, property, render)
  lazy val concurrent = Project("concurrent", file("concurrent"), settings = createSettings("sgine-concurrent"))
  lazy val core = Project("core", file("core"), settings = createSettings("sgine-core"))
  lazy val event = Project("event", file("event"), settings = createSettings("sgine-event"))
    .dependsOn(concurrent)
  lazy val math = Project("math", file("math"), settings = createSettings("sgine-math"))
  lazy val openglGenerator = Project("opengl-generator", file("opengl/generator"), settings = createSettings("sgine-opengl-generator"))
    .settings(libraryDependencies ++= Seq(
      "org.lwjgl" % "lwjgl" % "2.7.1",
      "com.googlecode.reflective" %% "reflective" % "1.0"
    ))
    .dependsOn(core)
  lazy val openglApi = Project("opengl-api", file("opengl/api"), settings = createSettings("sgine-opengl-api"))
    .dependsOn(core)
  lazy val openglLwjgl = Project("opengl-lwjgl", file("opengl/lwjgl"), settings = createSettings("sgine-opengl-lwjgl"))
    .dependsOn(openglApi)
    .settings(libraryDependencies += "org.lwjgl" % "lwjgl" % lwjglVersion)
  lazy val openglAndroid = Project("opengl-android", file("opengl/android"), settings = createSettings("sgine-opengl-android"))
    .dependsOn(openglApi)
  lazy val openglNehe = Project("opengl-nehe", file("opengl/nehe"), settings = createSettings("sgine-opengl-nehe"))
    .dependsOn(openglApi, openglLwjgl)
  lazy val property = Project("property", file("property"), settings = createSettings("sgine-property"))
  lazy val render = Project("render", file("render"), settings = createSettings("sgine-render"))
    .dependsOn(math, openglApi, openglAndroid, openglLwjgl)
}