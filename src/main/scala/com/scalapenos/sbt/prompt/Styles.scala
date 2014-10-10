package com.scalapenos.sbt.prompt

case class Style(background: Color = Colors.NoColor, foreground: Color = Colors.NoColor) {
  def render(text: String) = s"${background.bg}${foreground.fg}${text}${Colors.NoColor.bg}${Colors.NoColor.fg}"

  def bg(color: Color) = copy(background = color)
  def bg(color: Int) = copy(background = Color(color))

  def fg(color: Color) = copy(foreground = color)
  def fg(color: Int) = copy(foreground = Color(color))
}

trait Styles extends Colors {
  val NoStyle = Style()

  def bg(color: Color) = Style(background = color)
  def bg(color: Int) = Style(background = Color(color))

  def fg(color: Color) = Style(foreground = color)
  def fg(color: Int) = Style(foreground = Color(color))
}

object Styles extends Styles
