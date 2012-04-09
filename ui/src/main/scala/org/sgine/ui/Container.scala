package org.sgine.ui

import org.sgine.scene.MutableContainer

/**
 * Container is specifically targeted to contain Component children.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Container extends AbstractContainer with MutableContainer[Component]