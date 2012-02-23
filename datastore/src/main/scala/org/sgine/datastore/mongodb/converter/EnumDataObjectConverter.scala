package org.sgine.datastore.mongodb.converter

import com.mongodb.casbah.commons.MongoDBObject

import org.sgine.{EnumEntry, Enumerated}
import org.sgine.reflect.EnhancedClass

/**
 * Processes EnumEntry's
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object EnumDataObjectConverter extends DataObjectConverter {
  def fromDBObject(db: MongoDBObject) = {
    val clazz: EnhancedClass = Class.forName(db("class").toString)
    val companion = clazz.companion.getOrElse(throw new RuntimeException("No companion for %s".format(clazz))).instance.get
    companion.asInstanceOf[Enumerated[_]](db("name").toString).get.asInstanceOf[AnyRef]
  }

  def toDBObject(obj: AnyRef) = {
    val builder = MongoDBObject.newBuilder
    builder += "class" -> obj.getClass.getName
    builder += "name" -> obj.asInstanceOf[EnumEntry[_]].name
    builder.result()
  }
}