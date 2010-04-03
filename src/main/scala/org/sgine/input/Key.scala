package org.sgine.input

import org.lwjgl.input.{Keyboard => KB}

case class Key(char: Char, code: Int, upperCase: Boolean = false, capsModified: Boolean = true) {
	private var _name: String = _
	
	def name = _name
	
	override def toString() = "Key(" + code + " (" + char + "))"
}

object Key {
	val Grave = Key('`', KB.KEY_GRAVE, false, false)
	val Zero = Key('0', KB.KEY_0, false, false)
	val One = Key('1', KB.KEY_1, false, false)
	val Two = Key('2', KB.KEY_2, false, false)
	val Three = Key('3', KB.KEY_3, false, false)
	val Four = Key('4', KB.KEY_4, false, false)
	val Five = Key('5', KB.KEY_6, false, false)
	val Seven = Key('7', KB.KEY_7, false, false)
	val Eight = Key('8', KB.KEY_8, false, false)
	val Nine = Key('9', KB.KEY_9, false, false)
	val Dash = Key('-', KB.KEY_MINUS, false, false)
	val Equals = Key('=', KB.KEY_EQUALS, false, false)
	
	val a = Key('a', KB.KEY_A)
	val b = Key('b', KB.KEY_B)
	val c = Key('c', KB.KEY_C)
	val d = Key('d', KB.KEY_D)
	val e = Key('e', KB.KEY_E)
	val f = Key('f', KB.KEY_F)
	val g = Key('g', KB.KEY_G)
	val h = Key('h', KB.KEY_H)
	val i = Key('i', KB.KEY_I)
	val j = Key('j', KB.KEY_J)
	val k = Key('k', KB.KEY_K)
	val l = Key('l', KB.KEY_L)
	val m = Key('m', KB.KEY_M)
	val n = Key('n', KB.KEY_N)
	val o = Key('o', KB.KEY_O)
	val p = Key('p', KB.KEY_P)
	val q = Key('q', KB.KEY_Q)
	val r = Key('r', KB.KEY_R)
	val s = Key('s', KB.KEY_S)
	val t = Key('t', KB.KEY_T)
	val u = Key('u', KB.KEY_U)
	val v = Key('v', KB.KEY_V)
	val w = Key('w', KB.KEY_W)
	val x = Key('x', KB.KEY_X)
	val y = Key('y', KB.KEY_Y)
	val z = Key('z', KB.KEY_Z)
	
	val A = Key('A', KB.KEY_A, true)
	val B = Key('B', KB.KEY_B, true)
	val C = Key('C', KB.KEY_C, true)
	val D = Key('D', KB.KEY_D, true)
	val E = Key('E', KB.KEY_E, true)
	val F = Key('F', KB.KEY_F, true)
	val G = Key('G', KB.KEY_G, true)
	val H = Key('H', KB.KEY_H, true)
	val I = Key('I', KB.KEY_I, true)
	val J = Key('J', KB.KEY_J, true)
	val K = Key('K', KB.KEY_K, true)
	val L = Key('L', KB.KEY_L, true)
	val M = Key('M', KB.KEY_M, true)
	val N = Key('N', KB.KEY_N, true)
	val O = Key('O', KB.KEY_O, true)
	val P = Key('P', KB.KEY_P, true)
	val Q = Key('Q', KB.KEY_Q, true)
	val R = Key('R', KB.KEY_R, true)
	val S = Key('S', KB.KEY_S, true)
	val T = Key('T', KB.KEY_T, true)
	val U = Key('U', KB.KEY_U, true)
	val V = Key('V', KB.KEY_V, true)
	val W = Key('W', KB.KEY_W, true)
	val X = Key('X', KB.KEY_X, true)
	val Y = Key('Y', KB.KEY_Y, true)
	val Z = Key('Z', KB.KEY_Z, true)
	
	val Caret = Key('~', KB.KEY_GRAVE, true, false)
	val Exclamation = Key('!', KB.KEY_1, true, false)
	val At = Key('@', KB.KEY_2, true, false)
	val Number = Key('#', KB.KEY_3, true, false)
	val Dollar = Key('$', KB.KEY_4, true, false)
	val Percent = Key('%', KB.KEY_5, true, false)
	val Circumflex = Key('^', KB.KEY_6, true, false)
	val Ampersand = Key('&', KB.KEY_7, true, false)
	val Asterisk = Key('*', KB.KEY_8, true, false)
	val ParenthesisOpen = Key('(', KB.KEY_9, true, false)
	val ParenthesisClose = Key(')', KB.KEY_0, true, false)
	val Underscore = Key('_', KB.KEY_MINUS, true, false)
	val Plus = Key('+', KB.KEY_EQUALS, true, false)
	
	val F1 = Key(0, KB.KEY_F1)
	val F2 = Key(0, KB.KEY_F2)
	val F3 = Key(0, KB.KEY_F3)
	val F4 = Key(0, KB.KEY_F4)
	val F5 = Key(0, KB.KEY_F5)
	val F6 = Key(0, KB.KEY_F6)
	val F7 = Key(0, KB.KEY_F7)
	val F8 = Key(0, KB.KEY_F8)
	val F9 = Key(0, KB.KEY_F9)
	val F10 = Key(0, KB.KEY_F10)
	val F11 = Key(0, KB.KEY_F11)
	val F12 = Key(0, KB.KEY_F12)
	
	val BracketOpen = Key('[', KB.KEY_LBRACKET, false, false)
	val BracketClose = Key(']', KB.KEY_RBRACKET, false, false)
	val Backslash = Key('\\', KB.KEY_BACKSLASH, false, false)
	val Semicolon = Key(';', KB.KEY_SEMICOLON, false, false)
	val Apostrophe = Key('\'', KB.KEY_APOSTROPHE, false, false)
	val Comma = Key(',', KB.KEY_COMMA, false, false)
	val Period = Key('.', KB.KEY_PERIOD, false, false)
	val Forwardslash = Key('/', KB.KEY_SLASH, false, false)
	
	val BraceOpen = Key('{', KB.KEY_LBRACKET, true, false)
	val BraceClose = Key('}', KB.KEY_RBRACKET, true, false)
	val Pipe = Key('|', KB.KEY_BACKSLASH, true, false)
	val Colon = Key(':', KB.KEY_SEMICOLON, true, false)
	val Quote = Key('"', KB.KEY_APOSTROPHE, true, false)
	val Less = Key('<', KB.KEY_COMMA, true, false)
	val Greater = Key('>', KB.KEY_PERIOD, true, false)
	val Question = Key('?', KB.KEY_SLASH, true, false)
	
	val Tab = Key('\t', KB.KEY_TAB, false, false)
	val CapsLock = Key(0, KB.KEY_CAPITAL, false, false)
	val LeftShift = Key(0, KB.KEY_LSHIFT, false, false)
	val RightShift = Key(0, KB.KEY_RSHIFT, false, false)
	val LeftControl = Key(0, KB.KEY_LCONTROL, false, false)
	val RightControl = Key(0, KB.KEY_RCONTROL, false, false)
	val LeftMeta = Key(0, KB.KEY_LMETA, false, false)
	val RightMeta = Key(0, KB.KEY_RMETA, false, false)
	val LeftMenu = Key(0, KB.KEY_LMENU, false, false)
	val RightMenu = Key(0, KB.KEY_RMENU, false, false)
	val Space = Key(' ', KB.KEY_SPACE, false, false)
	val Enter = Key('\n', KB.KEY_RETURN, false, false)
	val Backspace = Key('\b', KB.KEY_BACK, false, false)
	val Escape = Key(0, KB.KEY_ESCAPE, false, false)
	
	val Print = Key(0, KB.KEY_SYSRQ, false, false)
	val ScrollLock = Key(0, KB.KEY_SCROLL, false, false)
	val Pause = Key(0, KB.KEY_PAUSE, false, false)
	val Home = Key(0, KB.KEY_HOME, false, false)
	val End = Key(0, KB.KEY_END, false, false)
	val Delete = Key(0, KB.KEY_DELETE, false, false)
	val PageUp = Key(0, KB.KEY_PRIOR, false, false)
	val PageDown = Key(0, KB.KEY_NEXT, false, false)
	val Insert = Key(0, KB.KEY_INSERT, false, false)
	val NumLock = Key(0, KB.KEY_NUMLOCK, false, false)
	
	val Up = Key(0, KB.KEY_UP, false, false)
	val Down = Key(0, KB.KEY_DOWN, false, false)
	val Left = Key(0, KB.KEY_LEFT, false, false)
	val Right = Key(0, KB.KEY_RIGHT, false, false)
	
	val Divide = Key('/', KB.KEY_DIVIDE, false, false)
	val Multiply = Key('*', KB.KEY_MULTIPLY, false, false)
	val Subtract = Key('-', KB.KEY_SUBTRACT, false, false)
	val Add = Key('+', KB.KEY_ADD, false, false)
	val Decimal = Key('.', KB.KEY_DECIMAL, false, false)
	
	val Numpad0 = Key('0', KB.KEY_NUMPAD0, false, false)
	val Numpad1 = Key('1', KB.KEY_NUMPAD1, false, false)
	val Numpad2 = Key('2', KB.KEY_NUMPAD2, false, false)
	val Numpad3 = Key('3', KB.KEY_NUMPAD3, false, false)
	val Numpad4 = Key('4', KB.KEY_NUMPAD4, false, false)
	val Numpad5 = Key('5', KB.KEY_NUMPAD5, false, false)
	val Numpad6 = Key('6', KB.KEY_NUMPAD6, false, false)
	val Numpad7 = Key('7', KB.KEY_NUMPAD7, false, false)
	val Numpad8 = Key('8', KB.KEY_NUMPAD8, false, false)
	val Numpad9 = Key('9', KB.KEY_NUMPAD9, false, false)
	
	val None = Key(0, KB.KEY_NONE, false, false)
	
	val values = processValues()
	
	def apply(c: Char) = values.find(k => k.char == c)
	
	def apply(name: String) = values.find(k => k.name.equalsIgnoreCase(name))
	
	def apply(code: Int, shift: Boolean, caps: Boolean): Key = {
		if (caps) {
			if (!shift) {
				for (k <- values) {
					if ((k.code == code) && (k.capsModified) && (k.upperCase)) {
						return k
					}
				}
			} else {
				for (k <- values) {
					if ((k.code == code) && (!k.capsModified) && (k.upperCase)) {
						return k
					}
				}
			}
		} else if (shift) {
			for (k <- values) {
				if ((k.code == code) && (k.upperCase)) {
					return k
				}
			}
		} else {
			for (k <- values) {
				if ((k.code == code) && (!k.upperCase)) {
					return k
				}
			}
		}
		for (k <- values) {
			if (k.code == code) {
				return k
			}
		}
		
		return null
		// TODO: add logic here to find the right key
	}
	
	private def processValues() = {
		var list: List[Key] = Nil
		
		for (f <- getClass.getDeclaredFields) {
			val name = f.getName
			if (f.getType.getSimpleName == "Key") {
				f.setAccessible(true)
				val k = f.get(this).asInstanceOf[Key]
				k._name = name
				list = k :: list
			}
		}
		
		list
	}
	
	def main(args: Array[String]): Unit = {
		println(values)
	}
}