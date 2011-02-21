import sbt._

class SgineProject(info: ProjectInfo) extends ParentProject(info) {
  lazy val core = project("core", "core")
  lazy val opengl = project("opengl", "opengl", new OpenGLProject(_), core)
  lazy val render = project("render", "render", core, opengl)

  class OpenGLProject(info: ProjectInfo) extends ParentProject(info) {
    lazy val generator = project("generator", "generator", core)
  }
}