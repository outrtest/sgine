package org.sgine.input

import org.sgine.{Enumerated, EnumEntry}


/**
 * Key represents keys on the keyboard.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
sealed case class Key(keyCode: Int) extends EnumEntry[Key]

object Key extends Enumerated[Key] {
  val AnyKey = Key(-1)
  val Zero = Key(7)
  val One = Key(8)
  val Two = Key(9)
  val Three = Key(10)
  val Four = Key(11)
  val Five = Key(12)
  val Six = Key(13)
  val Seven = Key(14)
  val Eight = Key(15)
  val Nine = Key(16)
  val A = Key(29)
  val AltLeft = Key(57)
  val AltRight = Key(58)
  val Apostrophe = Key(75)
  val At = Key(77)
  val B = Key(30)
  val Back = Key(4)
  val Backslash = Key(73)
  val C = Key(31)
  val Call = Key(5)
  val Camera = Key(27)
  val Clear = Key(28)
  val Comma = Key(55)
  val D = Key(32)
  val Delete = Key(67)
  val Backspace = Key(67)
  val ForwardDelete = Key(112)
  val Center = Key(23)
  val Down = Key(20)
  val Left = Key(21)
  val Right = Key(22)
  val Up = Key(19)
  val E = Key(33)
  val EndCall = Key(6)
  val Enter = Key(66)
  val Envelope = Key(65)
  val Equals = Key(70)
  val Explorer = Key(64)
  val F = Key(34)
  val Focus = Key(80)
  val G = Key(35)
  val Grave = Key(68)
  val H = Key(36)
  val HeadsetHook = Key(79)
  val Home = Key(3)
  val I = Key(37)
  val J = Key(38)
  val K = Key(39)
  val L = Key(40)
  val LeftBracket = Key(71)
  val M = Key(41)
  val MediaFastForward = Key(90)
  val MediaNext = Key(87)
  val MediaPlayPause = Key(85)
  val MediaPrevious = Key(88)
  val MediaRewind = Key(89)
  val MediaStop = Key(86)
  val Menu = Key(82)
  val Minus = Key(69)
  val Mute = Key(91)
  val N = Key(42)
  val Notification = Key(83)
  val Num = Key(78)
  val O = Key(43)
  val P = Key(44)
  val Period = Key(56)
  val Plus = Key(81)
  val Pound = Key(18)
  val Power = Key(26)
  val Q = Key(45)
  val R = Key(46)
  val RightBracket = Key(72)
  val S = Key(47)
  val Search = Key(84)
  val Semicolon = Key(74)
  val ShiftLeft = Key(59)
  val ShiftRight = Key(60)
  val Slash = Key(76)
  val SoftLeft = Key(1)
  val SoftRight = Key(2)
  val Space = Key(62)
  val Star = Key(17)
  val Sym = Key(63)
  val T = Key(48)
  val Tab = Key(61)
  val U = Key(49)
  val Unknown = Key(0)
  val V = Key(50)
  val VolumeDown = Key(25)
  val VolumeUp = Key(24)
  val W = Key(51)
  val X = Key(52)
  val Y = Key(53)
  val Z = Key(54)
  val MetaAltLeftOn = Key(16)
  val MetaAltOn = Key(2)
  val MetaAltRightOn = Key(32)
  val MetaShiftLeftOn = Key(64)
  val MetaShiftOn = Key(1)
  val MetaShiftRightOn = Key(128)
  val MetaSymOn = Key(4)
  val ControlLeft = Key(129)
  val ControlRight = Key(130)
  val Escape = Key(131)
  val End = Key(132)
  val Insert = Key(133)
  val PageUp = Key(92)
  val PageDown = Key(93)
  val PictSymbols = Key(94)
  val SwitchCharset = Key(95)
  val ButtonCircle = Key(255)
  val ButtonA = Key(96)
  val ButtonB = Key(97)
  val ButtonC = Key(98)
  val ButtonX = Key(99)
  val ButtonY = Key(100)
  val ButtonZ = Key(101)
  val ButtonL1 = Key(102)
  val ButtonR1 = Key(103)
  val ButtonL2 = Key(104)
  val ButtonR2 = Key(105)
  val ButtonThumbLeft = Key(106)
  val ButtonThumbRight = Key(107)
  val ButtonStart = Key(108)
  val ButtonSelect = Key(109)
  val ButtonMode = Key(110)
  val Colon = Key(243)
  val F1 = Key(244)
  val F2 = Key(245)
  val F3 = Key(246)
  val F4 = Key(247)
  val F5 = Key(248)
  val F6 = Key(249)
  val F7 = Key(250)
  val F8 = Key(251)
  val F9 = Key(252)
  val F10 = Key(253)
  val F11 = Key(254)
  val F12 = Key(255)

  def byKeyCode(keyCode: Int) = values.find(k => k.keyCode == keyCode)
}