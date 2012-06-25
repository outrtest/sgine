package org.sgine.datastore.converter

import org.sgine.{EnumEntry, Enumerated}
import org.sgine.reflect.EnhancedClass
import com.mongodb.{BasicDBObject, DBObject}
import org.sgine.datastore.DatastoreCollection

/**
 * Processes EnumEntry's
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object EnumDataObjectConverter extends DataObjectConverter {
  def fromDBObject(db: DBObject, collection: DatastoreCollection[_]) = {
    val clazz: EnhancedClass = Class.forName(db.get("class").toString)
    val companion = clazz.companion.getOrElse(throw new RuntimeException("No companion for %s".format(clazz))).instance.get
    companion.asInstanceOf[Enumerated[_]](db.get("name").toString).asInstanceOf[AnyRef]
  }

  def toDBObject(obj: AnyRef, collection: DatastoreCollection[_]) = {
    val dbo = new BasicDBObject()
    dbo.put("class", obj.getClass.getName)
    dbo.put("name", obj.asInstanceOf[EnumEntry[_]].name)
    dbo
  }
}