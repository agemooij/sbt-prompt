package com.scalapenos.sbt.prompt

/**
 * Represents the ANSI 256 colorspace. For some good references, see:
 *
 * - Ansi escape code reference: http://misc.flogisoft.com/bash/tip_colors_and_formatting
 * - Graphical overview of the Ansi 256 color codes: http://www.calmar.ws/vim/color-output.png
 */
sealed trait Color {
  val bg: String
  val fg: String
}

private case class ColorImpl(code: Int) extends Color {
  require((code >= 0 && code <= 255))

  val bg = s"\u001b[48;5;${code}m"
  val fg = s"\u001b[38;5;${code}m"
}

object Color {
  def apply(code: Int): Color = ColorImpl(code)
}

trait Colors {
  case object NoColor extends Color {
    val bg = "\u001b[49m"
    val fg = "\u001b[39m"
  }

  val red = Color(9)
  val green = Color(10)
  val yellow = Color(11)
  val blue = Color(12)
  val magenta = Color(13)
  val cyan = Color(14)
  val white = Color(15)
}

object Colors extends Colors
