package com.scalapenos.sbt.prompt

/**
 * A Style is a simple DSL-like wrapper around Color that combines a
 * foreground color with a background color. It can render a String
 * of text using these combined colors. The rendered String will
 * always be "closed" with the NoColor foreground/background color.
 */
case class Style(background: Color = Colors.NoColor, foreground: Color = Colors.NoColor) {
  def render(text: String) = s"${background.bg}${foreground.fg}${text}${Colors.NoColor.bg}${Colors.NoColor.fg}"

  def bg(color: Color) = copy(background = color)
  def bg(color: Int) = copy(background = Color(color))

  def fg(color: Color) = copy(foreground = color)
  def fg(color: Int) = copy(foreground = Color(color))
}

/** Simple mixin trait providing easy access to Style factory methods. */
trait Styles extends Colors {
  val NoStyle = Style()

  def bg(color: Color) = Style(background = color)
  def bg(color: Int) = Style(background = Color(color))

  def fg(color: Color) = Style(foreground = color)
  def fg(color: Int) = Style(foreground = Color(color))
}

object Styles extends Styles
