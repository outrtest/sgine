package org.sgine.datastore.mongodb

import org.sgine.datastore.{Datastore, Identifiable}
import com.mongodb.casbah.MongoDB
import java.util.UUID

import org.sgine.datastore.mongodb.converter._
import com.mongodb.casbah.commons.Implicits._
import org.sgine.reflect.EnhancedClass
import com.mongodb.casbah.commons.{MongoDBObjectBuilder, MongoDBObject}
import com.mongodb.DBObject
import org.bson.types.ObjectId

/**
 * MongoDB implementation of Datastore
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class MongoDBDatastore[T <: Identifiable](db: MongoDB, collectionName: String, val layers: Datastore[_]*)(implicit val manifest: Manifest[T]) extends Datastore[T] {
  private lazy val collection = db(collectionName)

  protected def persistInternal(obj: T) = {
    val dbObj = DataObjectConverter.toDBObject(obj)
    collection += dbObj
  }

  protected def deleteInternal(id: UUID) = collection.findAndRemove(MongoDBObject("_id" -> id)) != None

  override def clear() = collection.dropCollection()

  def commit() {} // TODO: support

  def rollback() {} // TODO: support

  def byExample(example: T) = {
    val ec = EnhancedClass(example.getClass)
    val method = ec.createMethod.getOrElse(throw new NullPointerException("%s is not a case class".format(example)))
    val companion = ec.companion.getOrElse(throw new NullPointerException("No companion found for %s".format(example)))
    val companionInstance = companion.instance.getOrElse(throw new NullPointerException("No companion instance found for %s".format(companion)))
    val defaults = method.args.collect {
      // Generate defaults excluding "id"
      case arg if (arg.name != "id") => arg.default(companionInstance) match {
        case None => arg.name -> arg.`type`.defaultForType // Default by the class type
        case Some(value) => arg.name -> value // Default argument for this case class
      }
    }.toMap
    val builder = MongoDBObject.newBuilder
    ec.caseValues.foreach(cv => if (defaults(cv.name) != cv[Any](example)) {
      val value = cv[Any](example)
      builder += cv.name -> DataObjectConverter.toDBValue(value)
    })
    val query = toDotNotation("", wrapDBObj(builder.result()))
    collection.find(query).map(entry => {
      DataObjectConverter.fromDBValue(entry)
    }).asInstanceOf[Iterator[T]]
  }

  override def byId(id: UUID) = {
    val criteria = MongoDBObject("_id" -> id)
    collection.findOne(criteria).map(entry => {
      DataObjectConverter.fromDBValue(entry).asInstanceOf[T]
    })
  }

  def all = collection.find().map(entry => DataObjectConverter.fromDBValue(entry)).asInstanceOf[Iterator[T]]

  private def toDotNotation(pre: String, values: MongoDBObject, builder: MongoDBObjectBuilder = MongoDBObject.newBuilder): DBObject = {
    if (values.isEmpty) {
      builder.result()
    } else {
      values.head match {
        case (s, id: ObjectId) => // Ignore for queries
        case (s, obj: DBObject) => toDotNotation(dotSeparate(pre, s), obj, builder)
        case (s, value) => builder += dotSeparate(pre, s) -> value
      }
      toDotNotation(pre, map2MongoDBObject(values.tail), builder)
    }
  }

  private def dotSeparate(pre: String, actual: String) = if (pre.nonEmpty) {
    "%s.%s".format(pre, actual)
  } else {
    actual
  }
}
