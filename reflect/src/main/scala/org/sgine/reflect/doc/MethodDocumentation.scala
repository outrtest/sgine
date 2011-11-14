package org.sgine.reflect.doc

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class MethodDocumentation(args: List[DocumentedClass],
                               returnClass: DocumentedClass,
                               url: String,
                               docs: Option[Documentation])