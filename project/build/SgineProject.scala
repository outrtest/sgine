import sbt._

import reaktor.scct.ScctProject

class SgineProject(info: ProjectInfo) extends ParentProject(info) {
  val lwjglRepo = "LWJGL Repository" at "http://adterrasperaspera.com/lwjgl/"
  val lwjglVersion = "2.7.1"

  lazy val concurrent = project("concurrent", "sgine-concurrent")
  lazy val core = project("core", "sgine-core", new CoreProject(_))
  lazy val event = project("event", "sgine-event")
  lazy val opengl = project("opengl", "sgine-opengl", new OpenGLProject(_))
  lazy val property = project("property", "sgine-property")
  lazy val render = project("render", "sgine-render", opengl)

  class CoreProject(info: ProjectInfo) extends DefaultProject(info) with ScctProject

  class OpenGLProject(info: ProjectInfo) extends ParentProject(info) {
    lazy val generator = project("generator", "sgine-opengl-generator", new OpenGLGeneratorProject(_))
    lazy val api = project("api", "sgine-opengl-api")
    lazy val lwjgl = project("lwjgl", "sgine-opengl-lwjgl", new OpenGLLWJGLProject(_), api)
    lazy val android = project("android", "sgine-opengl-android", new OpenGLAndroidProject(_), api)
  }

  class OpenGLGeneratorProject(info: ProjectInfo) extends DefaultProject(info) {
    lazy val lwjgl = "org.lwjgl" % "lwjgl" % lwjglVersion
  }

  class OpenGLLWJGLProject(info: ProjectInfo) extends DefaultProject(info) {
    lazy val lwjgl = "org.lwjgl" % "lwjgl" % lwjglVersion
  }

  class OpenGLAndroidProject(info: ProjectInfo) extends DefaultProject(info)
}