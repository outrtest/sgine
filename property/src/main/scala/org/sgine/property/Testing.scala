package org.sgine.property


object Testing {
  val property = Property[Long]()

  def main(args: Array[String]): Unit = {
    implicit def s2l(s: String) = s.toLong

    val test = Property[String]()
    property bind test
    test := "5"
    println(property.value)

    //    val destination = 100000000L
    //    val time = System.currentTimeMillis()
    //    while (property.value < destination) {
    //      property.value = property.value + 1
    //    }
    //    println("Took %s seconds".format((System.currentTimeMillis() - time) / 1000.0))
  }
}











