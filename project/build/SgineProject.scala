import sbt._

import reaktor.scct.ScctProject

class SgineProject(info: ProjectInfo) extends ParentProject(info) {
  val publishTo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"
  val sourceArtifact = Artifact.sources(artifactID)
  val docsArtifact = Artifact.javadoc(artifactID)

  val lwjglRepo = "LWJGL Repository" at "http://adterrasperaspera.com/lwjgl/"
  val lwjglVersion = "2.7.1"
  val reflectiveVersion = "1.0"

  lazy val concurrent = project("concurrent", "sgine-concurrent", new SgineProjectBase(_))
  lazy val core = project("core", "sgine-core", new CoreProject(_))
  lazy val event = project("event", "sgine-event", new SgineProjectBase(_))
  lazy val opengl = project("opengl", "sgine-opengl", new OpenGLProject(_))
  lazy val properties = project("property", "sgine-property", new SgineProjectBase(_))
  lazy val render = project("render", "sgine-render", new SgineProjectBase(_), opengl.api)

  override def deliverProjectDependencies = super.deliverProjectDependencies.toList - opengl.projectID

  Credentials(Path.userHome / ".ivy2" / ".credentials", log)

  class CoreProject(info: ProjectInfo) extends SgineProjectBase(info) with ScctProject

  class OpenGLProject(info: ProjectInfo) extends ParentProject(info) {
    lazy val generator = project("generator", "sgine-opengl-generator", new OpenGLGeneratorProject(_))
    lazy val api = project("api", "sgine-opengl-api", new SgineProjectBase(_), core)
    lazy val lwjgl = project("lwjgl", "sgine-opengl-lwjgl", new OpenGLLWJGLProject(_), api)
    lazy val android = project("android", "sgine-opengl-android", new OpenGLAndroidProject(_), api)
    lazy val nehe = project("nehe", "sgine-opengl-nehe", new OpenGLNeHeProject(_), api, lwjgl)
  }

  class OpenGLGeneratorProject(info: ProjectInfo) extends SgineProjectBase(info) {
    lazy val lwjgl = "org.lwjgl" % "lwjgl" % lwjglVersion
    lazy val reflective = "com.googlecode.reflective" %% "reflective" % reflectiveVersion
  }

  class OpenGLLWJGLProject(info: ProjectInfo) extends SgineProjectBase(info) {
    lazy val lwjgl = "org.lwjgl" % "lwjgl" % lwjglVersion
  }

  class OpenGLAndroidProject(info: ProjectInfo) extends SgineProjectBase(info)

  class OpenGLNeHeProject(info: ProjectInfo) extends SgineProjectBase(info) {
  }

  class SgineProjectBase(info: ProjectInfo) extends DefaultProject(info) {
    override def packageDocsJar = defaultJarPath("-javadoc.jar")
    override def packageSrcJar = defaultJarPath("-sources.jar")
    override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageDocs, packageSrc)
    override def managedStyle = ManagedStyle.Maven
  }
}