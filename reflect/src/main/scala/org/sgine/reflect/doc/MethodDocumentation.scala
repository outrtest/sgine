package org.sgine.reflect.doc

/**
 * MethodDocumentation represents documentation about a specific method.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class MethodDocumentation(args: List[DocumentedClass],
                               returnClass: DocumentedClass,
                               url: String,
                               docs: Option[Documentation])