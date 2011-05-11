/*
 * Copyright (c) 2011 Sgine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 *  Neither the name of 'Sgine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sgine.opengl.generator

import org.lwjgl.opengl._
import javax.microedition.khronos.opengles.GL10
import java.io.{FileWriter, File}
import com.googlecode.reflective._
import com.googlecode.reflective.doc.DocumentationReflection
import java.lang.reflect.Method

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object OpenGLGenerator {
  val showNoMappings = false
  val debugMethodName = "glClipPlane"

  def main(args: Array[String]): Unit = {
    // Run in offline mode
    DocumentationReflection.remoteSources = false

    val apiBase = new File("opengl/api/src/main/scala/org/sgine/opengl/")
    val androidBase = new File("opengl/android/src/main/scala/org/sgine/opengl/android/")
    val lwjglBase = new File("opengl/lwjgl/src/main/scala/org/sgine/opengl/lwjgl/")
    generate(apiBase, androidBase, lwjglBase)
  }

  def generate(apiBase: File, androidBase: File, lwjglBase: File) = {
    // Determine the OpenGL method names we need to support
    val glMethodNames = GLNameGenerator.generate()

    // Populate Android methods and fields into ClassExtractor
    val androidExtractor = new ClassExtractor(false) {
      classes = List(classOf[GL10], classOf[javax.microedition.khronos.opengles.GL11])

      process()
    }

     // Populate LWJGL methods and fields into ClassExtractor
    val lwjglExtractor = new ClassExtractor(true) {
      classes = List(classOf[GL11], classOf[GL12], classOf[GL13], classOf[GL14], classOf[GL15], classOf[GL20],
                    classOf[GL21], classOf[GL30], classOf[GL31], classOf[GL32], classOf[GL33], classOf[GL40],
                    classOf[GL41])

      process()
    }

    // Combine methods and fields from Android and LWJGL to create a compatible base framework
    val combiner = new Combiner(glMethodNames, androidExtractor, lwjglExtractor)
    println("GL Names: " + glMethodNames.length + ", No Matches: " + combiner.noMatches.length)
    println("Android: " + androidExtractor.methods.length + ", Unused: " + combiner.unusedAndroid.length)
    println("LWJGL: " + lwjglExtractor.methods.length + ", Unused: " + combiner.unusedLWJGL.length)
    println("Methods: " + combiner.methods.length)

    var map = Map.empty[String, Int]
    for (m <- combiner.methods) {
      val i = map.getOrElse(m.matcher, 0) + 1
      map += (m.matcher -> i)
    }
    map.foreach(t => println("  " + t._1 + ": " + t._2))

    // Debug info
    val printMethod = (s: String) => {
      val printJavaMethod = (m: Method) => {
        val em = method2EnhancedMethod(m)
        print("      " + em)
        if (combiner.usesMethod(m)) {
          print(" *** USED")
        }
        println()
      }
      println("  " + s)
      println("    Android:")
      androidExtractor.methods(s).foreach(printJavaMethod)
      println("    LWJGL:")
      lwjglExtractor.methods(s).foreach(printJavaMethod)
    }
    if (showNoMappings) {
      println("No Mappings: " + combiner.noMappings.length)
      combiner.noMappings.foreach(printMethod)
    }
    if (debugMethodName != null) {
      println("Debug Method: " + debugMethodName)
      printMethod(debugMethodName)
    }

    // Generate classes
    val creator = new ClassCreator(combiner)
    val api = creator.generateAPI()
    val android = creator.generateAndroid()
    val lwjgl = creator.generateLWJGL()

    // Write classes
    write(api, new File(apiBase, "GL.scala"))
    write(android, new File(androidBase, "GL.scala"))
    write(lwjgl, new File(lwjglBase, "GL.scala"))
  }

  private def write(s: String, file: File) = {
    val fw = new FileWriter(file)
    try {
      fw.write(s)
    } finally {
      fw.close()
    }
  }
}