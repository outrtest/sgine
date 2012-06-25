import sbt._
import Keys._
//import AndroidKeys._

import Dependencies._

object SgineBuild extends Build {
  val baseSettings = Defaults.defaultSettings ++ Seq(
    version := "1.0-SNAPSHOT",
    organization := "org.sgine",
    scalaVersion := "2.9.1",
    libraryDependencies ++= Seq(
      scalaTest
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
    libraryDependencies ++= Seq(neodatisOdb, mongodb),
    unmanagedSourceDirectories in Compile <<= baseDirectory apply {
      dir =>
        Seq(dir / "concurrent" / "src" / "main" / "scala",
          dir / "core" / "src" / "main" / "scala",
          dir / "datastore" / "src" / "main" / "scala",
          dir / "event" / "src" / "main" / "scala",
          dir / "input" / "src" / "main" / "scala",
          dir / "media" / "src" / "main" / "scala",
          dir / "property" / "src" / "main" / "scala",
          dir / "reflect" / "src" / "main" / "scala",
          dir / "scene" / "src" / "main" / "scala",
          dir / "ui" / "src" / "main" / "scala"
        )
    })
    .aggregate(concurrent, core, datastore, event, input, media, property, reflect, scene, ui)
  lazy val concurrent = Project("concurrent", file("concurrent"),
    settings = createSettings("sgine-concurrent"))
    .dependsOn(core, reflect)
  lazy val core = Project("core", file("core"), settings = createSettings("sgine-core"))
    .dependsOn(reflect)
  lazy val datastore = Project("datastore", file("datastore"), settings = createSettings("sgine-datastore"))
    .settings(libraryDependencies ++= Seq(neodatisOdb, mongodb))
    .dependsOn(core, reflect, event)
  lazy val event = Project("event", file("event"), settings = createSettings("sgine-event"))
    .dependsOn(concurrent, core)
  lazy val input = Project("input", file("input"), settings = createSettings("sgine-input"))
    .dependsOn(core, event, property)
  lazy val media = Project("media", file("media"), settings = createSettings("sgine-media"))
    .dependsOn(ui)
  lazy val property = Project("property", file("property"),
    settings = createSettings("sgine-property"))
    .dependsOn(event, scene)
  lazy val reflect = Project("reflect", file("reflect"), settings = createSettings("sgine-reflect"))
    .settings(libraryDependencies += paranamer)
  lazy val scene = Project("scene", file("scene"), settings = createSettings("sgine-scene"))
    .dependsOn(event)
  lazy val ui = Project("ui", file("ui"), settings = createSettings("sgine-ui"))
    .settings(publishArtifact in Test := true)
    .dependsOn(core, event, input, property, scene)
  /*lazy val uiExample = Project("ui-example", file("ui/example"), settings = createSettings("sgine-ui-example") ++
    AndroidProject.androidSettings ++
    TypedResources.settings ++
    AndroidMarketPublish.settings ++ Seq(
      keyalias in Android := "sgine_example",
      keystorePath in Android := file("../keys/sgine_example.keystore"),
      proguardOption in Android := """-keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
      }"""
    ))
    .dependsOn(ui)*/
}

object Dependencies {
  val neodatisOdb = "org.neodatis.odb" % "neodatis-odb" % "1.9.30.689"
  val paranamer = "com.thoughtworks.paranamer" % "paranamer" % "2.4"
  val scalaTest = "org.scalatest" % "scalatest_2.9.1" % "1.7.1" % "test"
  val mongodb = "org.mongodb" % "mongo-java-driver" % "2.8.0"
}