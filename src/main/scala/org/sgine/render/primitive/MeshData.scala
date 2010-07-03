package org.sgine.render.primitive

import _root_.org.sgine.core.Color
import java.util.ArrayList
import org.sgine.math.{Vector2, Vector3}
import util.Random

/**
 * Contains mutable data for a model, including vertexes, normals, texture coordinates, and indexes.
 */
class MeshData {

  private val vertexes = new ArrayList[Vector3]()
  private val normals = new ArrayList[Vector3]()
  private val textureCoordinates = new ArrayList[Vector2]()
  private val colors = new ArrayList[Color]()
  private val indexes = new ArrayList[Int]()

  private var nextFreeIndex = 0

  def getVertex(index: Int): Vector3 = vertexes.get(index)
  def getNormal(index: Int): Vector3 = normals.get(index)
  def getTextureCoordinate(index: Int): Vector2 = textureCoordinates.get(index)
  def getColor(index: Int): Color = colors.get(index)
  def getIndex(i: Int): Int = indexes.get(i)

  def nrOfVertexes: Int = vertexes.size
  def nrOfIndexes: Int = indexes.size

  /* Adds a vertex and returns its ordinal number. */
  def addVertex(pos: Vector3, normal: Vector3 = Vector3.UnitY, textureCoordinate: Vector2 = Vector2.Zero, color: Color = Color.White): Int = {
    vertexes.add(pos)
    normals.add(normal)
    textureCoordinates.add(textureCoordinate)
    colors.add(color)
    nextIndex()
  }

  def setVertex(index: Int, pos: Vector3, normal: Vector3 = null, textureCoordinate: Vector2 = null, color: Color = null) {
    if (pos != null) vertexes.set(index, pos)
    if (normal != null) normals.set(index, normal)
    if (textureCoordinate != null) textureCoordinates.set(index, textureCoordinate)
    if (color != null) colors.set(index, color)
  }

  def setNormal(index: Int, normal: Vector3) {
    normals.set(index, normal)
  }

  def setTextureCoordinate(index: Int, textureCoordinate: Vector2) {
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