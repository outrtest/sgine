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

package org.sgine.resource

import java.net.URL

/**
 * Resource represents a path-based lookup system for URLs along with additional convenience functionality.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Resource private(val url: URL) {
  /**
   * The parent resource to this resource
   */
  lazy val parent = new Resource(new URL(url.toString.substring(0, url.toString.lastIndexOf("/"))))

  /**
   * Generates a child Resource based on the name passed.
   */
  def child(name: String) = new Resource(new URL(url.toString + "/" + name))

  override def toString() = "Resource(" + url + ")"
}

object Resource {
  private var paths: List[ResourcePath] = Nil
  addPath("")
  addPath("", FileResourceFinder)

  /**
   * Adds a search path for resource lookup.
   */
  def addPath(path: String, finder: ResourceFinder = ClassLoaderResourceFinder) = {
    var p = path
    if ((!p.endsWith("/")) && (!p.endsWith("\\"))) {
      // Make sure path ends with slash
      p = p + "/"
    }
    paths = ResourcePath(path, finder) :: paths
  }

  /**
   * Generates a Resource based on an explicit URL.
   */
  def apply(url: URL) = new Resource(url)

  /**
   * Searches the Resource paths for the resource delineated by the given <code>name</code>.
   *
   * Throws a runtime exception if lookup fails.
   *
   * @return Resource
   */
  def apply(name: String) = get(name).getOrElse(throw new RuntimeException("Resource lookup failed: " + name))

  /**
   * Searches the Resource paths for the resource delineated by the given <code>name</code>
   *
   * @return Option[Resource]
   */
  def get(name: String) = paths.toStream.flatMap(rp => rp.find(name)).headOption

  /**
   * Generates a Resource based on the supplied File.
   */
  def apply(file: java.io.File) = new Resource(file.toURI.toURL)
}