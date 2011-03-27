import sbt._

import reaktor.scct.ScctProject

class SgineProject(info: ProjectInfo) extends ParentProject(info) {
  val lwjglRepo = "LWJGL Repository" at "http://adterrasperaspera.com/lwjgl/"
  val lwjglVersion = "2.7.1"

  lazy val core = project("core", "core", new CoreProject(_))
  lazy val opengl = project("opengl", "opengl", new OpenGLProject(_), core)
  lazy val render = project("render", "render", core, opengl)

  class CoreProject(info: ProjectInfo) extends DefaultProject(info) with ScctProject

  class OpenGLProject(info: ProjectInfo) extends ParentProject(info) {
    lazy val generator = project("generator", "generator", new OpenGLGeneratorProject(_), core)
    lazy val api = project("api", "api", core)
    lazy val lwjgl = project("lwjgl", "lwjgl", new OpenGLLWJGLProject(_), api)
    lazy val android = project("android", "android", new OpenGLAndroidProject(_), api)
  }

  class OpenGLGeneratorProject(info: ProjectInfo) extends DefaultProject(info) {
    lazy val lwjgl = "org.lwjgl" % "lwjgl" % lwjglVersion
  }

  class OpenGLLWJGLProject(info: ProjectInfo) extends DefaultProject(info) {
    lazy val lwjgl = "org.lwjgl" % "lwjgl" % lwjglVersion
  }

  class OpenGLAndroidProject(info: ProjectInfo) extends DefaultProject(info)
}