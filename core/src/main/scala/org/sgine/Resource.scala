package org.sgine

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.Gdx

/**
 * Resource represents an internal file resource.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Resource private(protected[sgine] val handle: FileHandle)

object Resource {
  def apply(name: String) = new Resource(Gdx.files.internal(name))
}