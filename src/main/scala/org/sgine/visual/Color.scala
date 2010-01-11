package org.sgine.visual

class Color private() {
	protected var _value: Int = _
	
	/**
	 * Value represents the backing Int value for the color in ARGB (0xaarrggbb)
	 */
	def value = _value
	
	def alpha = (value >> 24 & 0xff) / 255.0
	def red = (value >> 16 & 0xff) / 255.0
	def green = (value >> 8 & 0xff) / 255.0
	def blue = (value >> 0 & 0xff) / 255.0
	
	def hex = value.toHexString
	
	private def this(_value: Int) = {
		this()
		
		this._value = _value
	}
}

object Color {
	val AliceBlue = Color(0xf0f8ff)
	val AntiqueWhite = Color(0xfaebd7)
	val Aquamarine = Color(0x7fffd4)
	val Azure = Color(0xf0ffff)
	val Beige = Color(0xf5f5dc)
	val Bisque = Color(0xffe4c4)
	val Black = Color(0x000000)
	val BlanchedAlmond = Color(0xffebcd)
	val Blue = Color(0x0000ff)
	val BlueViolet = Color(0x8a2be2)
	val Brown = Color(0xa52a2a)
	val Burlywood = Color(0xdeb887)
	val CadetBlue = Color(0x5f9ea0)
	val Chartreuse = Color(0x7fff00)
	val Chocolate = Color(0xd2691e)
	val Coral = Color(0xff7f50)
	val CornflowerBlue = Color(0x6495ed)
	val Cornsilk = Color(0xfff8dc)
	val Cyan = Color(0x00ffff)
	val DarkBlue = Color(0x00008b)
	val DarkGoldenrod = Color(0xb8860b)
	val DarkGray = Color(0x3f3f3f)
	val DarkGreen = Color(0x006400)
	val DarkKhaki = Color(0xbdb76b)
	val DarkOlive_green = Color(0x556b2f)
	val DarkOrange = Color(0xff8c00)
	val DarkOrchid = Color(0x9932cc)
	val DarkRed = Color(0x8b0000)
	val DarkSalmon = Color(0xe9967a)
	val DarkSea_green = Color(0x8fbc8f)
	val DarkSlate_blue = Color(0x483d8b)
	val DarkSlate_gray = Color(0x2f4f4f)
	val DarkTurquoise = Color(0x00ced1)
	val DarkViolet = Color(0x9400d3)
	val DeepPink = Color(0xff1493)
	val DeepSky_blue = Color(0x00bfff)
	val DimGray = Color(0x696969)
	val DodgerBlue = Color(0x1e90ff)
	val Firebrick = Color(0xb22222)
	val FloralWhite = Color(0xfffaf0)
	val ForestGreen = Color(0x228b22)
	val Gainsboro = Color(0xdcdcdc)
	val GhostWhite = Color(0xf8f8ff)
	val Gold = Color(0xffd700)
	val Goldenrod = Color(0xdaa520)
	val Gray = Color(0xbebebe)
	val Green = Color(0x00ff00)
	val GreenYellow = Color(0xadff2f)
	val HaloBlue = Color(0x93a9b4)
	val HighlightBlue = Color(0xb2e1ff)
	val Honeydew = Color(0xf0fff0)
	val HotPink = Color(0xff69b4)
	val IndianRed = Color(0xcd5c5c)
	val Ivory = Color(0xfffff0)
	val Khaki = Color(0xf0e68c)
	val Lavender = Color(0xe6e6fa)
	val LavenderBlush = Color(0xfff0f5)
	val LawnGreen = Color(0x7cfc00)
	val LemonChiffon = Color(0xfffacd)
	val LightBlue = Color(0xadd8e6)
	val LightCoral = Color(0xf08080)
	val LightCyan = Color(0xe0ffff)
	val LightGoldenrod = Color(0xeedd82)
	val LightGoldenrod_yellow = Color(0xfafad2)
	val LightGray = Color(0xd3d3d3)
	val LightPink = Color(0xffb6c1)
	val LightSalmon = Color(0xffa07a)
	val LightSea_green = Color(0x20b2aa)
	val LightSky_blue = Color(0x87cefa)
	val LightSlate_blue = Color(0x8470ff)
	val LightSlate_gray = Color(0x778899)
	val LightSteel_blue = Color(0xb0c4de)
	val LightYellow = Color(0xffffe0)
	val LimeGreen = Color(0x32cd32)
	val Linen = Color(0xfaf0e6)
	val Magenta = Color(0xff00ff)
	val Maroon = Color(0xb03060)
	val MediumAquamarine = Color(0x66cdaa)
	val MediumBlue = Color(0x0000cd)
	val MediumOrchid = Color(0xba55d3)
	val MediumPurple = Color(0x9370db)
	val MediumSea_green = Color(0x3cb371)
	val MediumSlate_blue = Color(0x7b68ee)
	val MediumSpring_green = Color(0x00fa9a)
	val MediumTurquoise = Color(0x48d1cc)
	val MediumViolet_red = Color(0xc71585)
	val MidnightBlue = Color(0x191970)
	val MintCream = Color(0xf5fffa)
	val MistyRose = Color(0xffe4e1)
	val Moccasin = Color(0xffe4b5)
	val NavajoWhite = Color(0xffdead)
	val NavyBlue = Color(0x000080)
	val OldLace = Color(0xfdf5e6)
	val OliveDrab = Color(0x6b8e23)
	val Orange = Color(0xffa500)
	val OrangeRed = Color(0xff4500)
	val Orchid = Color(0xda70d6)
	val PaleGoldenrod = Color(0xeee8aa)
	val PaleGreen = Color(0x98fb98)
	val PaleTurquoise = Color(0xafeeee)
	val PaleViolet_red = Color(0xdb7093)
	val PapayaWhip = Color(0xffefd5)
	val PeachPuff = Color(0xffdab9)
	val Peru = Color(0xcd853f)
	val Pink = Color(0xffc0cb)
	val Plum = Color(0xdda0dd)
	val PowderBlue = Color(0xb0e0e6)
	val Purple = Color(0xa020f0)
	val Red = Color(0xff0000)
	val RosyBrown = Color(0xbc8f8f)
	val RoyalBlue = Color(0x4169e1)
	val SaddleBrown = Color(0x8b4513)
	val Salmon = Color(0xfa8072)
	val SandyBrown = Color(0xf4a460)
	val SeaGreen = Color(0x2e8b57)
	val Seashell = Color(0xfff5ee)
	val SelectBlue = Color(0x7fceff)
	val Sienna = Color(0xa0522d)
	val SkyBlue = Color(0x87ceeb)
	val SlateBlue = Color(0x6a5acd)
	val SlateGray = Color(0x708090)
	val Snow = Color(0xfffafa)
	val SpringGreen = Color(0x00ff7f)
	val SteelBlue = Color(0x4682b4)
	val Tan = Color(0xd2b48c)
	val Thistle = Color(0xd8bfd8)
	val Tomato = Color(0xff6347)
	val Turquoise = Color(0x40e0d0)
	val UmmGold = Color(0xffcc33)
	val UmmMaroon = Color(0x660000)
	val Violet = Color(0xee82ee)
	val VioletRed = Color(0xd02090)
	val Wheat = Color(0xf5deb3)
	val White = Color(0xffffff)
	val WhiteSmoke = Color(0xf5f5f5)
	val Yellow = Color(0xffff00)
	val YellowGreen = Color(0x9acd32)
	
	val Clear = Color(0x00ffffff)
	
	def random = values(new scala.util.Random().nextInt(values.length))
	lazy val values = loadValues()
	
	def apply(value: Int) = new Color(value)
	
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