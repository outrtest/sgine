package org.sgine.opengl.generator

import java.io.File
import org.lwjgl.opengl._
import javax.microedition.khronos.opengles.GL10

/**
 * 
 *
 * Date: 2/8/11
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Generator {
  def main(args: Array[String]): Unit = {
    generate(null)
  }

  def generate(output: File) = {
    // Populate LWJGL methods and fields into ClassExtractor
    val lwjglExtractor = new ClassExtractor(true) {
      classes = List(classOf[GL11], classOf[GL12], classOf[GL13], classOf[GL14], classOf[GL15], classOf[GL20],
                    classOf[GL21], classOf[GL30], classOf[GL31], classOf[GL32], classOf[GL33], classOf[GL40],
                    classOf[GL41])

      process()
    }
    println("LWJGL: " + lwjglExtractor.methods.length)

    // Populate Android methods and fields into ClassExtractor
    val androidExtractor = new ClassExtractor(false) {
      classes = List(classOf[GL10], classOf[javax.microedition.khronos.opengles.GL11])

      process()
    }
    println("Android: " + androidExtractor.methods.length)

    val glMethodNames = GLNameGenerator.generate()

    // Combine methods and fields from Android and LWJGL to create a compatible base interface
    val combiner = new Combiner(glMethodNames, androidExtractor, lwjglExtractor)
    println("Combined: " + combiner.methods.length)

    val tc = new GLCreator(combiner)
//    println(tc.string)
  }

  def convertClass(c: Class[_]) = c.getName match {
    case "int" => "Int"
    case "float" => "Float"
    case "void" => "Unit"
    case s => throw new RuntimeException("Cannot convert class: " + s)
  }
}