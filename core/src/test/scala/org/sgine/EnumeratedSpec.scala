package org.sgine

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class EnumeratedSpec extends FlatSpec with ShouldMatchers {
  "TestEnum" should "have three entries" in {
    TestEnum.size should be(3)
  }

  it should  "have One as the first entry" in {
    TestEnum.One.ordinal should be(0)
    TestEnum(0) should be(TestEnum.One)
  }

  it should  "have Two as the second entry" in {
    TestEnum(1) should be(TestEnum.Two)
    TestEnum.Two.ordinal should be(1)
  }

  it should  "have Three as the third entry" in {
    TestEnum(2) should be(TestEnum.Three)
    TestEnum.Three.ordinal should be(2)
  }

  it should "not equal other entries" in {
    TestEnum.One should not equal(TestEnum.Two)
    TestEnum.Two should not equal(TestEnum.Three)
    TestEnum.Three should not equal(TestEnum.One)
  }
}

sealed class TestEnum extends EnumEntry

object TestEnum extends Enumerated[TestEnum] {
  val One = new TestEnum
  val Two = new TestEnum
  val Three = new TestEnum
}