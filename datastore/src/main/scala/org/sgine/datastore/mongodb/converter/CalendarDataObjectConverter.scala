package org.sgine.datastore.mongodb.converter

import com.mongodb.casbah.commons.MongoDBObject
import java.util.Calendar

/**
 * @author Matt Hicks <matt@outr.com>
 */
object CalendarDataObjectConverter extends DataObjectConverter {
  def fromDBObject(db: MongoDBObject) = {
    val c = Calendar.getInstance()
    c.setTimeInMillis(db("time").toString.toLong)
    c
  }

  def toDBObject(obj: AnyRef) = {
    val builder = MongoDBObject.newBuilder
    builder += "class" -> obj.getClass.getName
    builder += "time" -> obj.asInstanceOf[Calendar].getTimeInMillis.toString
    builder.result()
  }
}
