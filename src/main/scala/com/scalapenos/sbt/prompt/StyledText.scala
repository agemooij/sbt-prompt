package com.scalapenos.sbt.prompt

/**
 * A StyledText instance represents the combination of some
 * String of text and a Style that can be rendered together.
 */
sealed trait StyledText {
  def text: String
  def style: Style
  def isEmpty: Boolean

  /**
   * Creates a new instance of StyledText withe same Style as
   * the original and the text tranformed using the specified
   * function.
   */
  def mapText(f: String ⇒ String): StyledText

  lazy val rendered = style.render(text)
  override def toString = rendered
}

object StyledText {
  def apply(text: String, style: Style = Styles.NoStyle): StyledText = NonEmpty(text, style)

  case class NonEmpty(text: String, style: Style = Styles.NoStyle) extends StyledText {
    val isEmpty = false
    def mapText(f: String ⇒ String) = copy(text = f(text))
  }

  /**
   * The Empty StyledText has no Style and no text, so mapText will
   * not work on it. Handy for when you only want to add
   * prefixes/suffixes when the text is actually non-empty, like for
   * instance with the current Git branch name.
   */
  case object Empty extends StyledText {
    val text = ""
    val style = Styles.NoStyle

    val isEmpty = true
    def mapText(f: String ⇒ String) = this
  }
}
