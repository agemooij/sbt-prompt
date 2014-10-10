package com.scalapenos.sbt.prompt

import sbt._
import Keys._

/**
 * A PromptTheme is composed from a collection of Promptlets and an optional Separator.
 * It can render itself into a prompt string based on the current SBT State.
 */
case class PromptTheme(promptlets: Seq[Promptlet], separator: Separator = Separators.NoSeparator) {
  def render(state: State): String = {
    val styled = promptlets.map(_.render(state)).filterNot(_.isEmpty)
    val separated = new StringBuilder
    var previous: StyledText = null

    for (next ← styled) {
      if (previous == null) {
        separated ++= next.rendered
      } else {
        separated ++= separator.render(previous, next).rendered
        separated ++= next.rendered
      }

      previous = next
    }

    separated.toString
  }
}

case class Promptlet(content: State ⇒ StyledText) {
  def render(state: State): StyledText = content(state)
  def mapText(f: String ⇒ String) = Promptlet(content.andThen(_.mapText(f)))

  def pad(padding: String) = mapText(text ⇒ padding + text + padding)
  def padLeft(prefix: String) = mapText(text ⇒ prefix + text)
  def padRight(suffix: String) = mapText(text ⇒ text + suffix)
}

case class Separator(text: String, styleCombiner: (Style, Style) ⇒ Style) {
  def render(previous: StyledText, next: StyledText) = StyledText(text, styleCombiner(previous.style, next.style))
}

trait Separators extends Styles {
  val NoSeparator = Separator("", (_, _) ⇒ NoStyle)
}
object Separators extends Separators
