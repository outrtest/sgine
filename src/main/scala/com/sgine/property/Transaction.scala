package com.sgine.property

import java.util._
import scala.collection.JavaConversions._

class Transaction {
	private val map = new HashMap[Property[_], TransactionValue[_]]
	
	def +=[T](p:Property[T], value:T):Transaction = {
		map.put(p, new TransactionValue[T](p, value))
		
		this
	}
	
	def -=[T](p:Property[T]) = {
		map.remove(p)
	}
	
	def commit() = {
		map.values().foreach(_.changeValue())
		map.values().foreach(_.invokeChange())
		map.clear()
	}
	
	def rollback() = {
		map.clear()
	}
}

case class TransactionValue[T] (p:Property[T], value:T) {
	def changeValue() = {
		p match {
			case cp:ChangeableProperty[_] => {
				cp(value, false)
			}
			case _ =>
		}
	}
	
	def invokeChange() = {
		p match {
			case cp:ChangeableProperty[_] => {
				cp.changed()
			}
			case _ => p := value
		}
	}
}