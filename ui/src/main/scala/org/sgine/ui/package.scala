package org.sgine

import com.badlogic.gdx.Gdx

package object ui {
  implicit def resource2FileHandle(resource: Resource) = Gdx.files.internal(resource.path)
}