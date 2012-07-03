package org.sgine

import com.badlogic.gdx.Gdx
import org.powerscala.Resource

package object ui {
  implicit def resource2FileHandle(resource: Resource) = Gdx.files.internal(resource.path)
}