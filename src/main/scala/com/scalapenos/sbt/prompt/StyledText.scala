package com.scalapenos.sbt.prompt

sealed trait StyledText {
  def text: String
  def style: Style
  def isEmpty: Boolean
  def mapText(f: String ⇒ String): StyledText

  lazy val rendered = style.render(text)
  override def toString = rendered
}

private case class StyledTextImpl(text: String, style: Style = Styles.NoStyle) extends StyledText {
  val isEmpty = false
  def mapText(f: String ⇒ String) = copy(text = f(text))
}

object StyledText {
  def apply(text: String, style: Style = Styles.NoStyle): StyledText = StyledTextImpl(text, style)

  case object Empty extends StyledText {
    val text = ""
    val style = Styles.NoStyle

    val isEmpty = true
    def mapText(f: String ⇒ String) = this
  }
}
