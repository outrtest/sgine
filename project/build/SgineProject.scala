import sbt._

import reaktor.scct.ScctProject

class SgineProject(info: ProjectInfo) extends ParentProject(info) {
  val lwjglRepo = "LWJGL Repository" at "http://adterrasperaspera.com/lwjgl/"

  lazy val core = project("core", "core", new CoreProject(_))
  lazy val opengl = project("opengl", "opengl", new OpenGLProject(_), core)
  lazy val render = project("render", "render", core, opengl)

  class CoreProject(info: ProjectInfo) extends DefaultProject(info) with ScctProject

  class OpenGLProject(info: ProjectInfo) extends ParentProject(info) {
    lazy val generator = project("generator", "generator", new OpenGLGeneratorProject(_), core)
  }

  class OpenGLGeneratorProject(info: ProjectInfo) extends DefaultProject(info) {
    lazy val lwjgl = "org.lwjgl" % "lwjgl" % "2.7.1"
  }
}