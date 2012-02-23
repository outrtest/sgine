package org.sgine.datastore.mongodb.converter

import org.sgine.EnumEntry
import org.bson.types.ObjectId
import com.mongodb.{BasicDBList, DBObject}
import annotation.tailrec
import com.mongodb.casbah.commons.{MongoDBList, MongoDBListBuilder, MongoDBObject}
import collection.mutable.ListBuffer

import com.mongodb.casbah.commons.Implicits._
import java.util.UUID

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait DataObjectConverter {
  def fromDBObject(db: MongoDBObject): AnyRef

  def toDBObject(obj: AnyRef): DBObject
}

object DataObjectConverter {
  private var map = Map.empty[Class[_], DataObjectConverter]

  def fromDBObject[T](db: MongoDBObject) = {
    val clazz = Class.forName(db.as[String]("class"))
    val converter = findConverter(clazz)
    converter.fromDBObject(db).asInstanceOf[T]
  }

  def toDBObject(obj: AnyRef) = {
    val converter = findConverter(obj.getClass)
    converter.toDBObject(obj)
  }

  private def findConverter(clazz: Class[_]) = {
    if (!map.contains(clazz)) {
      synchronized {
        if (!map.contains(clazz)) {
          val converter = if (classOf[EnumEntry[_]].isAssignableFrom(clazz)) {
            EnumDataObjectConverter
          } else {
            new ReflectiveDataObjectConverter(clazz)
          }
          map += clazz -> converter
        }
      }
    }
    map(clazz)
  }

  def toDBValue(obj: Any) = obj match {
    case objectId: ObjectId => objectId
    case uuid: UUID => uuid
    case seq: Seq[_] => toDBList(seq)
    case s: String => s
    case b: Boolean => b
    case b: Byte => b
    case c: Char => c
    case i: Int => i
    case l: Long => l
    case f: Float => f
    case d: Double => d
    case null => null
    case obj: AnyRef => toDBObject(obj)
  }

  def fromDBValue(obj: Any) = obj match {
    case objectId: ObjectId => objectId
    case uuid: UUID => uuid
    case dbList: BasicDBList => fromDBList(dbList)
    case s: String => s
    case b: Boolean => b
    case b: Byte => b
    case c: Char => c
    case i: Int => i
    case l: Long => l
    case f: Float => f
    case d: Double => d
    case null => null
    case obj: DBObject => fromDBObject(obj)
  }

  @tailrec
  private def toDBList(seq: Seq[_], listBuilder: MongoDBListBuilder = MongoDBList.newBuilder): BasicDBList = {
    if (seq.nonEmpty) {
      val head = toDBValue(seq.head.asInstanceOf[AnyRef])
      listBuilder += head
      toDBList(seq.tail, listBuilder)
    } else {
      listBuilder.result()
    }
  }

  private def fromDBList(dbList: BasicDBList): List[Any] = {
    val buffer = new ListBuffer[Any]
    for (v <- dbList) {
      buffer += fromDBValue(v)
    }
    buffer.toList
  }
}