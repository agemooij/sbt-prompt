package com.scalapenos

case class StyledText(text: String, style: Style = Styles.NoStyle) {
  lazy val rendered = style.render(text)
  override def toString = rendered

  def mapText(f: String ⇒ String) = copy(text = f(text))
}

/**
 * Represents the ANSI 256 colorspace. For some good references, see:
 *
 * - Ansi escape code reference: http://misc.flogisoft.com/bash/tip_colors_and_formatting
 * - Graphical overview of the Ansi 256 color codes: http://www.calmar.ws/vim/color-output.png
 */
case class Color(code: Int) {
  require((code >= 0 && code <= 255) || code == Color.DefaultCode)

  def bg = if (code == Color.DefaultCode) "\u001b[49m" else s"\u001b[48;5;${code}m"
  def fg = if (code == Color.DefaultCode) "\u001b[39m" else s"\u001b[38;5;${code}m"
}

object Color {
  val DefaultCode = -42
  val Default = Color(DefaultCode)

  val red = Color(9)
  val green = Color(10)
  val yellow = Color(11)
  val blue = Color(12)
  val magenta = Color(13)
  val cyan = Color(14)
  val white = Color(15)
}

case class Style(background: Color = Color.Default, foreground: Color = Color.Default) {
  def render(text: String) = s"${background.bg}${foreground.fg}${text}${Color.Default.bg}${Color.Default.fg}"

  def bg(color: Color) = copy(background = color)
  def bg(color: Int) = copy(background = Color(color))

  def fg(color: Color) = copy(foreground = color)
  def fg(color: Int) = copy(foreground = Color(color))

  def red = fg(Color.red)
  def green = fg(Color.green)
  def yellow = fg(Color.yellow)
  def blue = fg(Color.blue)
  def magenta = fg(Color.magenta)
  def cyan = fg(Color.cyan)
  def white = fg(Color.white)

  def bgRed = bg(Color.red)
  def bgGreen = bg(Color.green)
  def bgYellow = bg(Color.yellow)
  def bgBlue = bg(Color.blue)
  def bgMagenta = bg(Color.magenta)
  def bgCyan = bg(Color.cyan)
  def bgWhite = bg(Color.white)
}

trait Styles {
  val NoStyle = Style()

  def bg(color: Color) = Style(background = color)
  def bg(color: Int) = Style(background = Color(color))

  def fg(color: Color) = Style(foreground = color)
  def fg(color: Int) = Style(foreground = Color(color))

  val red = fg(Color.red)
  val green = fg(Color.green)
  val yellow = fg(Color.yellow)
  val blue = fg(Color.blue)
  val magenta = fg(Color.magenta)
  val cyan = fg(Color.cyan)
  val white = fg(Color.white)

  val bgRed = bg(Color.red)
  val bgGreen = bg(Color.green)
  val bgYellow = bg(Color.yellow)
  val bgBlue = bg(Color.blue)
  val bgMagenta = bg(Color.magenta)
  val bgCyan = bg(Color.cyan)
  val bgWhite = bg(Color.white)
}

object Styles extends Styles
