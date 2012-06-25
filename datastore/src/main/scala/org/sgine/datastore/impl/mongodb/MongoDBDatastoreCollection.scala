package org.sgine.datastore.impl.mongodb

import org.sgine.datastore._
import org.sgine.datastore.converter.DataObjectConverter
import com.mongodb.BasicDBObject

import scala.collection.JavaConversions._
import java.util
import scala.Some
import org.sgine.datastore.DatastoreQuery

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class MongoDBDatastoreCollection[T <: Persistable](val session: MongoDBDatastoreSession, val name: String)
                                                  (implicit val manifest: Manifest[T]) extends DatastoreCollection[T] {
  lazy val collection = session.database.getCollection(name)

  protected def persistInternal(ref: T) = {
    val dbo = DataObjectConverter.toDBObject(ref, this)
    ref.state match {
      case PersistenceState.NotPersisted => collection.insert(dbo)
      case PersistenceState.Persisted => {
        collection.findAndModify(new BasicDBObject("_id", ref.id), dbo)
      }
    }
  }

  protected def deleteInternal(ref: T) = {
    collection.findAndRemove(new BasicDBObject("_id", ref.id))
  }

  def byId(id: util.UUID) = DataObjectConverter.fromDBValue(collection.findOne(new BasicDBObject("_id", id)), this) match {
    case null => None
    case value => Some(value.asInstanceOf[T])
  }

  def executeQuery(query: DatastoreQuery[T]) = {
    val dbo = new BasicDBObject()
    val filters = query._filters.reverse
    filters.foreach {
      case filter => {
        val value = filter.operator match {
          case Operator.< => new BasicDBObject("$lt", filter.value)
          case Operator.<= => new BasicDBObject("$lte", filter.value)
          case Operator.> => new BasicDBObject("$gt", filter.value)
          case Operator.>= => new BasicDBObject("$gte", filter.value)
          case Operator.equal => filter.value
          case Operator.nequal => new BasicDBObject("$ne", filter.value)
        }
        dbo.put(filter.field.name, value)
      }
    }
    var cursor = collection.find(dbo)
    if (query._skip > 0) {
      cursor = cursor.skip(query._skip)
    }
    if (query._limit > 0) {
      cursor = cursor.limit(query._limit)
    }
    asScalaIterator(cursor).map(entry => DataObjectConverter.fromDBValue(entry, this)).asInstanceOf[Iterator[T]]
  }

  def iterator = asScalaIterator(collection.find()).map(entry => DataObjectConverter.fromDBValue(entry, this)).asInstanceOf[Iterator[T]]
}
