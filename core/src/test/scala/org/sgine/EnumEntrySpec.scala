package org.sgine

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class EnumEntrySpec extends WordSpec with ShouldMatchers {
  "TestEnum" should {
    "have three entries" in {
      TestEnum.values.length should be(3)
    }
    "have 'One' as the first entry" in {
      TestEnum.values(0).name should be("One")
    }
    "have 'Two' as the second entry" in {
      TestEnum.values(1).name should be("Two")
    }
    "have 'Three' as the third entry" in {
      TestEnum.values(2).name should be("Three")
    }
    "have 0 as the ordinal of One" in {
      TestEnum.One.ordinal should be(0)
    }
    "have 1 as the ordinal of Two" in {
      TestEnum.Two.ordinal should be(1)
    }
    "have 2 as the ordinal of Three" in {
      TestEnum.Three.ordinal should be(2)
    }
    "find 'One' by name" in {
      TestEnum.values("One") should be(TestEnum.One)
    }
    "find 'Two' by name" in {
      TestEnum.values("Two") should be(TestEnum.Two)
    }
    "find 'Three' by name" in {
      TestEnum.values("Three") should be(TestEnum.Three)
    }
    "not find equality between 'One' and 'Two'" in {
      TestEnum.One should not be TestEnum.Two
    }
    "not find equality between 'Two' and 'Three'" in {
      TestEnum.Two should not be TestEnum.Three
    }
    "not find equality between 'Three' and 'One'" in {
      TestEnum.Three should not be TestEnum.One
    }
  }
  "TestCombinableEnum" should {
    "combine 'Four' and 'Five'" in {
      val FourAndFive = TestCombinableEnum.combine(TestCombinableEnum.Four, TestCombinableEnum.Five)
      TestCombinableEnum.Four should be(FourAndFive)
      TestCombinableEnum.Five should be(FourAndFive)
      FourAndFive should be(TestCombinableEnum.Four)
      FourAndFive should be(TestCombinableEnum.Five)
    }
    "have three entries" in {
      TestCombinableEnum.values.length should be(3)
    }
    "have 'Four' as the first entry" in {
      TestCombinableEnum.values(0).name should be("Four")
    }
    "have 'Five' as the second entry" in {
      TestCombinableEnum.values(1).name should be("Five")
    }
    "have 'Six' as the third entry" in {
      TestCombinableEnum.values(2).name should be("Six")
    }
    "have 0 as the ordinal of Four" in {
      TestCombinableEnum.Four.ordinal should be(0)
    }
    "have 1 as the ordinal of Five" in {
      TestCombinableEnum.Five.ordinal should be(1)
    }
    "have 2 as the ordinal of Six" in {
      TestCombinableEnum.Six.ordinal should be(2)
    }
    "find 'Four' by name" in {
      TestCombinableEnum.values("Four") should be(TestCombinableEnum.Four)
    }
    "find 'Five' by name" in {
      TestCombinableEnum.values("Five") should be(TestCombinableEnum.Five)
    }
    "find 'Six' by name" in {
      TestCombinableEnum.values("Six") should be(TestCombinableEnum.Six)
    }
    "not find equality between 'Four' and 'Five'" in {
      TestCombinableEnum.Four should not be TestCombinableEnum.Five
    }
    "not find equality between 'Five' and 'Six'" in {
      TestCombinableEnum.Five should not be TestCombinableEnum.Six
    }
    "not find equality between 'Six' and 'Four'" in {
      TestCombinableEnum.Six should not be TestCombinableEnum.Four
    }
  }
}

sealed class TestEnum(override val ordinal: Int) extends EnumEntry[TestEnum]

object TestEnum extends Enumerated[TestEnum] {
  val One = new TestEnum(0)
  val Two = new TestEnum(1)
  val Three = new TestEnum(2)
}

sealed class TestCombinableEnum(override val ordinal: Int) extends EnumEntry[TestCombinableEnum]

object TestCombinableEnum extends EnumeratedCombinable[TestCombinableEnum] {
  val Four = new TestCombinableEnum(0)
  val Five = new TestCombinableEnum(1)
  val Six = new TestCombinableEnum(2)

  def combine(enums: TestCombinableEnum*) = new TestCombinableEnum(TestCombinableEnum.values.length) with Combined[TestCombinableEnum] {
    val combined = enums.toList
  }
}