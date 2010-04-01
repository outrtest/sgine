package org.sgine.core

class Color private[core]() {
	protected var _red: Double = _
	protected var _green: Double = _
	protected var _blue: Double = _
	protected var _alpha: Double = _
	
	def red = _red
	def green = _green
	def blue = _blue
	def alpha = _alpha
	
	def add(red: Double = 0.0, green: Double = 0.0, blue: Double = 0.0, alpha: Double = 0.0) = Color(_red + red, _green + green, _blue + blue, _alpha + alpha)
	def subtract(red: Double = 0.0, green: Double = 0.0, blue: Double = 0.0, alpha: Double = 0.0) = Color(_red - red, _green - green, _blue - blue, _alpha - alpha)
	def multiply(red: Double = 1.0, green: Double = 1.0, blue: Double = 1.0, alpha: Double = 1.0) = Color(_red * red, _green * green, _blue * blue, _alpha * alpha)
	def divide(red: Double = 1.0, green: Double = 1.0, blue: Double = 1.0, alpha: Double = 1.0) = Color(_red / red, _green / green, _blue / blue, _alpha / alpha)
	def set(red: Double = -1.0, green: Double = -1.0, blue: Double = -1.0, alpha: Double = -1.0) = {
		val r = if (red != -1.0) red else _red
		val g = if (green != -1.0) red else _green
		val b = if (blue != -1.0) red else _blue
		val a = if (alpha != -1.0) red else _alpha
		Color(r, g, b, a)
	}
	
	private def this(value: Int) = {
		this()
		
		_alpha = (value >> 24 & 0xff) / 255.0
		_red = (value >> 16 & 0xff) / 255.0
		_green = (value >> 8 & 0xff) / 255.0
		_blue = (value >> 0 & 0xff) / 255.0
	}
}

object Color {
	val AliceBlue = Color(0xfff08ff)
	val AntiqueWhite = Color(0xfffaebd7)
	val Aquamarine = Color(0xff7fffd4)
	val Azure = Color(0xfff0fff)
	val Beige = Color(0xfff5f5dc)
	val Bisque = Color(0xffffe4c4)
	val Black = Color(0xff000000)
	val BlanchedAlmond = Color(0xffffebcd)
	val Blue = Color(0xff0000f)
	val BlueViolet = Color(0xff8a2be2)
	val Brown = Color(0xffa52a2a)
	val Burlywood = Color(0xffdeb887)
	val CadetBlue = Color(0xff5f9ea0)
	val Chartreuse = Color(0xff7fff00)
	val Chocolate = Color(0xffd2691e)
	val Coral = Color(0xffff7f50)
	val CornflowerBlue = Color(0xff6495ed)
	val Cornsilk = Color(0xfffff8dc)
	val Cyan = Color(0xff00fff)
	val DarkBlue = Color(0xff00008b)
	val DarkGoldenrod = Color(0xffb8860b)
	val DarkGray = Color(0xff3f3f3f)
	val DarkGreen = Color(0xff006400)
	val DarkKhaki = Color(0xffbdb76b)
	val DarkOlive_green = Color(0xff556b2f)
	val DarkOrange = Color(0xffff8c00)
	val DarkOrchid = Color(0xff9932cc)
	val DarkRed = Color(0xff8b0000)
	val DarkSalmon = Color(0xffe9967a)
	val DarkSea_green = Color(0xff8fbc8f)
	val DarkSlate_blue = Color(0xff483d8b)
	val DarkSlate_gray = Color(0xff2f4f4f)
	val DarkTurquoise = Color(0xff00ced1)
	val DarkViolet = Color(0xff9400d3)
	val DeepPink = Color(0xffff1493)
	val DeepSky_blue = Color(0xff00bfff)
	val DimGray = Color(0xff696969)
	val DodgerBlue = Color(0xff1e90f)
	val Firebrick = Color(0xffb22222)
	val FloralWhite = Color(0xfffffaf0)
	val ForestGreen = Color(0xff228b22)
	val Gainsboro = Color(0xffdcdcdc)
	val GhostWhite = Color(0xfff8f8ff)
	val Gold = Color(0xffffd700)
	val Goldenrod = Color(0xffdaa520)
	val Gray = Color(0xffbebebe)
	val Green = Color(0xff00f00)
	val GreenYellow = Color(0xffadff2f)
	val HaloBlue = Color(0xff93a9b4)
	val HighlightBlue = Color(0xffb2e1ff)
	val Honeydew = Color(0xfff0ff0)
	val HotPink = Color(0xffff69b4)
	val IndianRed = Color(0xffcd5c5c)
	val Ivory = Color(0xfffffff0)
	val Khaki = Color(0xfff0e68c)
	val Lavender = Color(0xffe6e6fa)
	val LavenderBlush = Color(0xfffff05)
	val LawnGreen = Color(0xff7cfc00)
	val LemonChiffon = Color(0xfffffacd)
	val LightBlue = Color(0xffadd8e6)
	val LightCoral = Color(0xfff08080)
	val LightCyan = Color(0xffe0fff)
	val LightGoldenrod = Color(0xffeedd82)
	val LightGoldenrod_yellow = Color(0xfffafad2)
	val LightGray = Color(0xffd3d3d3)
	val LightPink = Color(0xffffb6c1)
	val LightSalmon = Color(0xffffa07a)
	val LightSea_green = Color(0xff20b2aa)
	val LightSky_blue = Color(0xff87cefa)
	val LightSlate_blue = Color(0xff8470f)
	val LightSlate_gray = Color(0xff778899)
	val LightSteel_blue = Color(0xffb0c4de)
	val LightYellow = Color(0xffffffe0)
	val LimeGreen = Color(0xff32cd32)
	val Linen = Color(0xfffaf0e6)
	val Magenta = Color(0xffff00f)
	val Maroon = Color(0xffb03060)
	val MediumAquamarine = Color(0xff66cdaa)
	val MediumBlue = Color(0xff0000cd)
	val MediumOrchid = Color(0xffba55d3)
	val MediumPurple = Color(0xff9370db)
	val MediumSea_green = Color(0xff3cb371)
	val MediumSlate_blue = Color(0xff7b68ee)
	val MediumSpring_green = Color(0xff00a9a)
	val MediumTurquoise = Color(0xff48d1cc)
	val MediumViolet_red = Color(0xffc71585)
	val MidnightBlue = Color(0xff191970)
	val MintCream = Color(0xfff5fffa)
	val MistyRose = Color(0xffffe4e1)
	val Moccasin = Color(0xffffe4b5)
	val NavajoWhite = Color(0xffffdead)
	val NavyBlue = Color(0xff000080)
	val OldLace = Color(0xfffdf5e6)
	val OliveDrab = Color(0xff6b8e23)
	val Orange = Color(0xffffa500)
	val OrangeRed = Color(0xffff4500)
	val Orchid = Color(0xffda70d6)
	val PaleGoldenrod = Color(0xffeee8aa)
	val PaleGreen = Color(0xff98fb98)
	val PaleTurquoise = Color(0xffafeeee)
	val PaleViolet_red = Color(0xffdb7093)
	val PapayaWhip = Color(0xffffefd5)
	val PeachPuff = Color(0xffffdab9)
	val Peru = Color(0xffcd853f)
	val Pink = Color(0xffffc0cb)
	val Plum = Color(0xffdda0dd)
	val PowderBlue = Color(0xffb0e0e6)
	val Purple = Color(0xffa0200)
	val Red = Color(0xffff0000)
	val RosyBrown = Color(0xffbc8f8f)
	val RoyalBlue = Color(0xff4169e1)
	val SaddleBrown = Color(0xff8b4513)
	val Salmon = Color(0xfffa8072)
	val SandyBrown = Color(0xfff4a460)
	val SeaGreen = Color(0xff2e8b57)
	val Seashell = Color(0xfffff5ee)
	val SelectBlue = Color(0xff7fceff)
	val Sienna = Color(0xffa0522d)
	val SkyBlue = Color(0xff87ceeb)
	val SlateBlue = Color(0xff6a5acd)
	val SlateGray = Color(0xff708090)
	val Snow = Color(0xfffffafa)
	val SpringGreen = Color(0xff00f7f)
	val SteelBlue = Color(0xff4682b4)
	val Tan = Color(0xffd2b48c)
	val Thistle = Color(0xffd8bfd8)
	val Tomato = Color(0xffff6347)
	val Turquoise = Color(0xff40e0d0)
	val UmmGold = Color(0xffffcc33)
	val UmmMaroon = Color(0xff660000)
	val Violet = Color(0xffee82ee)
	val VioletRed = Color(0xffd02090)
	val Wheat = Color(0xfff5deb3)
	val White = Color(0xffffffff)
	val WhiteSmoke = Color(0xfff5f5f5)
	val Yellow = Color(0xffffff00)
	val YellowGreen = Color(0xff9acd32)
	
	val Clear = Color(0x00fffff)
	
	def random = values(new scala.util.Random().nextInt(values.length))
	lazy val values = loadValues()
	
	def apply(value: Int) = new Color(value)
	
	def apply(red: Double = 0.0, green: Double = 0.0, blue: Double = 0.0, alpha: Double = 1.0) = {
		val c = new Color()
		c._red = red
		c._green = green
		c._blue = blue
		c._alpha = alpha
		c
	}
	
	private def loadValues() = {
		var values: List[Color] = Nil
		
		for (f <- getClass.getDeclaredFields) {
			if (f.getType == classOf[Color]) {
				f.setAccessible(true)
				values = f.get(this).asInstanceOf[Color] :: values
			}
		}
		
		values
	}
}