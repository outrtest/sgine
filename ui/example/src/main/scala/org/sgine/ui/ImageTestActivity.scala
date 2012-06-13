package org.sgine.ui

import android.os.Bundle
import com.badlogic.gdx.backends.android.{AndroidApplicationConfiguration, AndroidApplication}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ImageTestActivity extends AndroidApplication {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    val config = new AndroidApplicationConfiguration()
    config.useWakelock = true

    initialize(ImageExample.applicationListener, config)
  }
}
