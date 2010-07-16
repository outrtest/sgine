package org.sgine.render.primitive

import _root_.org.sgine.core.Color
import java.util.ArrayList
import util.Random

import simplex3d.math.doublem._

/**
 * Contains mutable data for a model, including vertexes, normals, texture coordinates, and indexes.
 */
class MeshData {

  private val vertexes = new ArrayList[Vec3d]()
  private val normals = new ArrayList[Vec3d]()
  private val textureCoordinates = new ArrayList[Vec2d]()
  private val colors = new ArrayList[Color]()
  private val indexes = new ArrayList[Int]()

  private var nextFreeIndex = 0

  def getVertex(index: Int): Vec3d = vertexes.get(index)
  def getNormal(index: Int): Vec3d = normals.get(index)
  def getTextureCoordinate(index: Int): Vec2d = textureCoordinates.get(index)
  def getColor(index: Int): Color = colors.get(index)
  def getIndex(i: Int): Int = indexes.get(i)

  def nrOfVertexes: Int = vertexes.size
  def nrOfIndexes: Int = indexes.size

  /* Adds a vertex and returns its ordinal number. */
  def addVertex(pos: Vec3d, normal: Vec3d = Vec3d.UnitY, textureCoordinate: Vec2d = Vec2d.Zero, color: Color = Color.White): Int = {
    vertexes.add(pos)
    normals.add(normal)
    textureCoordinates.add(textureCoordinate)
    colors.add(color)
    nextIndex()
  }

  def setVertex(index: Int, pos: Vec3d, normal: Vec3d = null, textureCoordinate: Vec2d = null, color: Color = null) {
    if (pos != null) vertexes.set(index, pos)
    if (normal != null) normals.set(index, normal)
    if (textureCoordinate != null) textureCoordinates.set(index, textureCoordinate)
    if (color != null) colors.set(index, color)
  }

  def setNormal(index: Int, normal: Vec3d) {
    normals.set(index, normal)
  }

  def setTextureCoordinate(index: Int, textureCoordinate: Vec2d) {
    textureCoordinates.set(index, textureCoordinate)
  }

  def setColor(index: Int, color: Color) {
    colors.set(index, color)
  }

  def addIndex(i: Int) {
    indexes.add(i)
  }

  def addTriangle(a: Int, b: Int, c: Int) {
    indexes.add(a)
    indexes.add(b)
    indexes.add(c)
  }

  def addQuad(a: Int, b: Int, c: Int, d: Int) {
    addTriangle(a, b, c)
    addTriangle(c, d, a)
  }

  def smoothAllNormals() {
    // TODO: Implement
  }


  def randomizeColors() {
    val rand = new Random()
    val max = colors.size
    var i = 0
    while (i < max) {
      colors.set(i, Color(rand.nextDouble, rand.nextDouble, rand.nextDouble))
      i += 1
    }
  }

  private def nextIndex(): Int = {
    val current = nextFreeIndex
    nextFreeIndex += 1
    current
  }
}