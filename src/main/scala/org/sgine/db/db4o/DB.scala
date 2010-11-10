package org.sgine.db.db4o

import com.db4o.Db4o
import com.db4o.ObjectContainer
import com.db4o.ObjectServer
import com.db4o.ObjectSet
import com.db4o.config.ObjectConstructor
import com.db4o.cs.Db4oClientServer
import com.db4o.internal.DefragmentContext
import com.db4o.internal.delete.DeleteContext
import com.db4o.marshall._
import com.db4o.query._
import com.db4o.reflect.ReflectClass
import com.db4o.typehandlers._

import java.io.File

import org.sgine.db.DBFactory

import scala.reflect.Manifest

class DB(server: ObjectServer) extends org.sgine.db.DB {
	protected def createTransaction() = new Transaction(this, server.openClient())
	
	def close() = server.close()
}

class Transaction(val db: DB, container: ObjectContainer) extends org.sgine.db.Transaction {
	def store[T](obj: T)(implicit manifest: Manifest[T]) = container.store(obj)
	
	def query[T](clazz: Class[T]) = new RichObjectSet(container.query(clazz))
	
	def query[T](predicate: T => Boolean, sortFunction: (T, T) => Int = null)(implicit manifest: Manifest[T]) = {
		val p = new Predicate[T](manifest.erasure.asInstanceOf[Class[T]]) {
			def `match`(entry: T) = predicate(entry)
		}
		
		new RichObjectSet(if (sortFunction != null) container.query(p, new RichQueryComparator(sortFunction)) else container.query(p))
	}
	
	def delete[T](obj: T)(implicit manifest: Manifest[T]) = container.delete(obj)
	
	def commit() = container.commit()
	
	def rollback() = container.rollback()
	
	def close() = container.close()
}

class RichObjectSet[T](objectSet: ObjectSet[T]) extends Iterator[T] {
	def hasNext = objectSet.hasNext
	def next = objectSet.next
}

class RichQueryComparator[T](sortFunction: (T, T) => Int) extends QueryComparator[T] {
	def compare(first: T, second: T) = sortFunction(first, second)
}

object DB extends DBFactory {
	def apply(file: File) = {
		val config = Db4oClientServer.newServerConfiguration
		config.common.optimizeNativeQueries(true)
		
		// Configure support for enums
		val predicate = new com.db4o.typehandlers.TypeHandlerPredicate() {
			def `match`(classReflector: ReflectClass) = {
				val c = Class.forName(classReflector.getName)
				val enum = classOf[org.sgine.core.Enum]
				enum.isAssignableFrom(c)
			}
		}
		val handler = new com.db4o.typehandlers.ValueTypeHandler() {
			def read(context: ReadContext): AnyRef = {
				val length = context.readInt()
				val classNameBytes = new Array[Byte](length)
				context.readBytes(classNameBytes)
				val className = new String(classNameBytes)
				val index = context.readInt()
				
				val c = Class.forName(className)
				val enumerated = c.getField("MODULE$").get(null).asInstanceOf[org.sgine.core.Enumerated[_]]
				val enum = enumerated(index)
				enum.asInstanceOf[AnyRef]
			}
			
			def defragment(context: DefragmentContext) = {
				// TODO: do I need to implement anything for this?
			}
			
			def delete(context: DeleteContext) = {
				// TODO: do I need to implement anything for this?
			}
			
			def write(context: WriteContext, obj: AnyRef) = obj match {
				case e: org.sgine.core.Enum => {
					val className = e.parent.getClass.getName
					val index = e.ordinal
					val classNameBytes = className.getBytes
					context.writeInt(classNameBytes.length)
					context.writeBytes(classNameBytes)
					context.writeInt(index)
				}
				case _ => throw new RuntimeException("Attempting to write: " + obj + " as an Enum!")
			}
		}
		config.common.registerTypeHandler(predicate, handler)
		
		val path = file match {
			case null => {		// Set to in-memory storage if file is null
				config.file().storage(new com.db4o.io.PagingMemoryStorage())
				null
			}
			case f => f.getAbsolutePath
		}
		new DB(Db4oClientServer.openServer(config, path, 0))
	}
}