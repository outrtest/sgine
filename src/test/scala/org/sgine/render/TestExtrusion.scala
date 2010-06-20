/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sgine.render

import org.sgine.math.mutable.Matrix4

import java.awt.Font
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import javax.imageio._

import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.render.primitive.Extrusion
import scala.collection.mutable.ListBuffer

object TestExtrusion {

	def main(args : Array[String]) : Unit = {

		val r = Renderer.createFrame(1024, 768, "Test Extrusion", 4, 8, 4, 4)
		r.verticalSync := false

		val t = TextureUtil(ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")))

		val m = Matrix4().translate(z = -1000.0).scaleAll(0.04)
		val i = RenderImage(t)

                val trans = new AffineTransform()
                trans.translate(-200, 0)
                trans.scale(1, -1)

                val font = new Font("Arial Unicode MS", Font.BOLD, 150).deriveFont(trans)
                val fontRenderContext = new FontRenderContext(null, true, true)
                val glyphVector = font.createGlyphVector(fontRenderContext, "Sgine!")

                val extrusions = Extrusion(glyphVector.getOutline, 50, Color.White, i, 1)

                val renderList = ListBuffer[() => Unit]()
                renderList +  MatrixState(m)
                extrusions.foreach(renderList + _)
                renderList + FPS(1.0)

		r.renderable := RenderList(renderList : _*)

		while(true) {
			Thread.sleep(5)
			m.rotate(0.005, 0.005, 0.0)
		}
	}
}