package org.sgine.datastore.mongodb.converter

import com.mongodb.casbah.commons.{MongoDBObjectBuilder, MongoDBObject}
import annotation.tailrec
import java.lang.reflect.Modifier
import org.sgine.reflect.{CaseValue, EnhancedMethod, EnhancedClass}

class ReflectiveDataObjectConverter(erasure: EnhancedClass) extends DataObjectConverter {
  private lazy val methods = generateMethods().toList

  def toDBObject(obj: AnyRef) = {
    val builder = MongoDBObject.newBuilder
    val clazz: EnhancedClass = obj.getClass
    if (clazz.isCase) {
      val cv2Builder = (cv: CaseValue) => {
        val name = cv.name match {
          case "id" => "_id"
          case s => s
        }
        val value = DataObjectConverter.toDBValue(cv.apply(obj))
        builder += name -> value
      }
      clazz.caseValues.foreach(cv2Builder)
    } else {
      throw new RuntimeException("Only case classes are supported: %s".format(clazz))
    }
    //    applyMethods(builder, obj, methods)
    builder += "class" -> clazz.name
    builder.result()
  }

  @tailrec
  private def applyMethods(builder: MongoDBObjectBuilder, obj: AnyRef, methods: List[EnhancedMethod]): Unit = {
    if (methods.nonEmpty) {
      val m = methods.head
      val name = m.name match {
        case "id" => "_id"
        case s => s
      }
      try {
        val value = DataObjectConverter.toDBValue(m.invoke[AnyRef](obj))
        builder += name -> value
      } catch {
        case exc => throw new RuntimeException("Error attempting to invoke method %s on object %s".format(m, obj), exc)
      }
      applyMethods(builder, obj, methods.tail)
    }
  }

  def fromDBObject(db: MongoDBObject): AnyRef = {
    val clazz: EnhancedClass = Class.forName(db.as[String]("class"))
    val values = clazz.caseValues.map(cv => {
      val name = cv.name match {
        case "id" => "_id"
        case s => s
      }
      val value = db.getOrElse(name, null)
      cv.name -> DataObjectConverter.fromDBValue(value)
    }).toMap
    try {
      clazz.create[AnyRef](values)
    } catch {
      case exc => throw new RuntimeException("Unable to instantiate %s with values %s".format(clazz, values), exc)
    }
  }

  private def generateMethods() = {
    erasure.methods.collect {
      case m if (validMethod(m)) => m
    }
  }

  private val filteredMethods = "toString, hashCode, productIterator, productElements, productPrefix, productArity, wait, converter"

  private def validMethod(m: EnhancedMethod) = {
    if (m.args.nonEmpty) {
      false
    } else if (m.isStatic) {
      false
    } else if (m.isNative) {
      false
    } else if (m.name.indexOf('$') != -1) {
      false
    } else if (Modifier.isTransient(m.javaMethod.getModifiers)) {
      false
    } else if (filteredMethods.contains(m.name)) {
      false
    } else {
      true
    }
  }
}