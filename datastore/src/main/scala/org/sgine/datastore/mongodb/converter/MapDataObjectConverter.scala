package org.sgine.datastore.mongodb.converter

import com.mongodb.casbah.commons.MongoDBObject
import scala.collection.JavaConversions._
import com.mongodb.DBObject

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object MapDataObjectConverter extends DataObjectConverter {
  def fromDBObject(db: MongoDBObject): AnyRef = {
    var map = Map.empty[Any, Any]
    val entries = db("count").toString.toInt
    val clazz: Class[_] = Class.forName(db.as[String]("class"))
    for (index <- 0 until entries) {
      val obj = db("entry%s".format(index)).asInstanceOf[MongoDBObject]
      val k = obj("key")
      val v = obj("value")
      val key = DataObjectConverter.fromDBValue(k)
      val value = DataObjectConverter.fromDBValue(v)
      map += key -> value
    }
    if (clazz.isAssignableFrom(classOf[Map[_, _]])) {
      map
    } else if (clazz == classOf[scala.collection.mutable.Map[_, _]]) {
      scala.collection.mutable.Map.empty[Any, Any] ++ map
    } else if (clazz.isAssignableFrom(classOf[java.util.Map[_, _]])) {
      val hashMap = new java.util.HashMap[Any, Any](entries)
      map.foreach(entry => hashMap.put(entry._1, entry._2))
      hashMap
      map
    } else {
      throw new RuntimeException("Unsupported map type: %s".format(clazz))
    }
  }

  def toDBObject(obj: AnyRef): DBObject = {
    val builder = MongoDBObject.newBuilder
    val map = obj match {
      case map: Map[_, _] => map
      case map: java.util.Map[_, _] => map.toMap
    }
    builder += "count" -> map.size
    map.zipWithIndex.foreach(container => {
      val entry = container._1
      val index = container._2
      val key = DataObjectConverter.toDBValue(entry._1)
      val value = DataObjectConverter.toDBValue(entry._2)
      val entryBuilder = MongoDBObject.newBuilder
      entryBuilder += "key" -> key
      entryBuilder += "value" -> value
      builder += "entry%s".format(index) -> entryBuilder.result()
    })
    builder += "class" -> obj.getClass.getName
    builder.result()
  }
}