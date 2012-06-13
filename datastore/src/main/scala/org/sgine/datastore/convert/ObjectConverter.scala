package org.sgine.datastore.convert

import xml.{Text, Elem}


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object ObjectConverter {
  val test = <fruits>
    <fruit>
      <name>apple</name>
      <taste>ok</taste>
    </fruit>
    <fruit>
      <name>banana</name>
      <taste>better</taste>
    </fruit>
  </fruits>

  //  def toXML(obj: T): Elem

//  def toJSON(obj: T) = {
//
//  }

  def xml2Data(elem: Elem): DataElement = {
    val name = elem.label
    if (elem.child.length == 1 && elem.child.head.isInstanceOf[Text]) {
      ValueElement(name, elem.text.trim)
    } else {
      ObjectElement(elem.label, elem.child.collect {
        case e: Elem => xml2Data(e)
      })
    }
  }

  def main(args: Array[String]): Unit = {
    val result = xml2Data(test)
    println(result)
  }
}

trait DataElement {
  def name: String
}

case class ObjectElement(name: String, elements: Seq[DataElement]) extends DataElement

case class ValueElement(name: String, value: String) extends DataElement