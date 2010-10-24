package org.sgine.property.style

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.sgine.property.AdvancedProperty

class StyleSpec extends FlatSpec with ShouldMatchers {
	val theme = new Theme()
	
	"TestStylized" should "have null for 's'" in {
		TestStylized.s() should equal(null)
		
		TestStylized.s2 := "Direct"
	}
	
	it should "directly apply TestStyle" in {
		TestStyle(TestStylized)
		
		TestStylized.s() should equal("Hello1")
		TestStylized.s2() should equal("Direct")
		TestStylized.s3() should equal("Hello3")
		TestStylized.s4() should equal("Hello4")
		TestStylized.s5() should equal("Hello5")
	}
	
	it should "update s3 when the style property changes" in {
		TestStyle.s3 := "Goodbye"
		
		TestStylized.s3.style should equal(TestStyle.s3)
		TestStylized.s3() should equal("Goodbye")
	}
	
	it should "directly remove styles" in {
		Style.remove(TestStylized)
		
		TestStylized.s() should equal(null)
		TestStylized.s2() should equal("Direct")
		TestStylized.s3() should equal(null)
		TestStylized.s4() should equal(null)
		TestStylized.s5() should equal(null)
	}
	
	it should "use Theme.current to apply styles" in {
		Theme.current := TestTheme
		
		TestStylized.s() should equal("Test1")
		TestStylized.s2() should equal("Direct")
		TestStylized.s3() should equal("Test3")
		TestStylized.s4() should equal("Test4")
		TestStylized.s5() should equal(null)
	}
	
	it should "use Theme.current to revert styles to null" in {
		Theme.current := null
		
		TestStylized.s() should equal(null)
		TestStylized.s2() should equal("Direct")
		TestStylized.s3() should equal(null)
		TestStylized.s4() should equal(null)
		TestStylized.s5() should equal(null)
	}
	
	it should "create a Theme on the fly and apply styles by name" in {
		val theme = new Theme()
		val style = new SelectorStyle(SelectorGroup(IdSelector("test")))
		style.register("s", "Dynamic!")
		style.register("s3", "Dynamic3!")
		theme.register(style)
		
		Theme.current := theme
		
		TestStylized.s() should equal("Dynamic!")
		TestStylized.s2() should equal("Direct")
		TestStylized.s3() should equal("Dynamic3!")
		TestStylized.s4() should equal(null)
		TestStylized.s5() should equal(null)
	}
	
	it should "use IdSelector to apply styles over two levels" in {
		Theme.current := TestTheme2
		
		TestStylized.s() should equal("IdSelector!")
		TestStylized.s2() should equal("Direct")
		TestStylized.s3() should equal(null)
		TestStylized.s4() should equal(null)
		TestStylized.s5() should equal(null)
	}
	
	it should "use ClassSelector to apply styles over two levels" in {
		Theme.current := TestTheme3
		
		TestStylized.s() should equal("ClassSelector!")
		TestStylized.s2() should equal("Direct")
		TestStylized.s3() should equal(null)
		TestStylized.s4() should equal(null)
		TestStylized.s5() should equal(null)
	}
	
	it should "use StyleNameSelector to apply styles over two levels" in {
		Theme.current := TestTheme4
		
		TestStylized.s() should equal("StyleNameSelector!")
		TestStylized.s2() should equal("Direct")
		TestStylized.s3() should equal(null)
		TestStylized.s4() should equal(null)
		TestStylized.s5() should equal(null)
	}
}

object ParentStylized extends Stylized {
	id := "testParent"
	style := "testParentStyle"
}

object TestStylized extends Stylized {
	override val parent = ParentStylized
	
	id := "test"
	style := "testStyle"
	
	val s = new AdvancedProperty[String](null, this)
	val s2 = new AdvancedProperty[String](null, this)
	val s3 = new AdvancedProperty[String](null, this)
	val s4 = new AdvancedProperty[String](null, this)
	val s5 = new AdvancedProperty[String](null, this)
}

object TestStyle extends Style {
	def condition(stylized: Stylized) = stylized.id() == "test"
		
	val s = "Hello1"
	val s2 = "Hello2"
	val s3 = new AdvancedProperty[String]("Hello3")
	def s4() = "Hello4"
	var s5 = "Hello5"
}

object TestTheme extends Theme {
	val s1 = new Style {
		def condition(stylized: Stylized) = stylized.id() == "test"
		val s = "Test1"
		val s2 = "Test2"
		val s3 = "Test3"
		val s4 = "Test4"
	}
	register(s1)
}

object TestTheme2 extends Theme {
	val groups = SelectorGroup(IdSelector("test"), IdSelector("testParent"))
	val s1 = new SelectorStyle(groups) {
		val s = "IdSelector!"
	}
	register(s1)
}

object TestTheme3 extends Theme {
	val groups = SelectorGroup(ClassSelector("TestStylized"), ClassSelector("ParentStylized"))
	val s1 = new SelectorStyle(groups) {
		val s = "ClassSelector!"
	}
	register(s1)
}

object TestTheme4 extends Theme {
	val groups = SelectorGroup(StyleNameSelector("testStyle"), StyleNameSelector("testParentStyle"))
	val s1 = new SelectorStyle(groups) {
		val s = "StyleNameSelector!"
	}
	register(s1)
}