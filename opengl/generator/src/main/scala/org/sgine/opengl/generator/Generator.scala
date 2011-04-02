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

    // Create GL trait and companion
    val glDirectory = new File("opengl/api/src/main/scala/org/sgine/opengl/")
    glDirectory.mkdirs()
    val glScala = new File(glDirectory, "GL.scala")
    var fw = new FileWriter(glScala)
    try {
      fw.write(tc.glString)
    } finally {
      fw.close()
    }

    // Create Android
    val androidDirectory = new File("opengl/android/src/main/scala/org/sgine/opengl/android/")
    androidDirectory.mkdirs()
    val androidScala = new File(androidDirectory, "GL.scala")
    fw = new FileWriter(androidScala)
    try {
      fw.write(tc.androidString)
    } finally {
      fw.close()
    }

    // Create LWJGL
    val lwjglDirectory = new File("opengl/lwjgl/src/main/scala/org/sgine/opengl/lwjgl/")
    lwjglDirectory.mkdirs()
    val lwjglScala = new File(lwjglDirectory, "GL.scala")
    fw = new FileWriter(lwjglScala)
    try {
      fw.write(tc.lwjglString)
    } finally {
      fw.close()
    }
  }

  def convertClass(c: Class[_]) = c.getName match {
    case "boolean" => "Boolean"
    case "byte" => "Byte"
    case "int" => "Int"
    case "float" => "Float"
    case "void" => "Unit"
    case "java.lang.String" => "String"
    case s => s
  }
}