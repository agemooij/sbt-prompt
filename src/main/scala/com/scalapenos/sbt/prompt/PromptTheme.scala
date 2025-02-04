package com.scalapenos.sbt.prompt

import sbt._

/**
 * A PromptTheme contains a sequence of Promptlets and an optional Separator.
 * It can render itself into a prompt string based on the current SBT State.
 */
case class PromptTheme(promptlets: Seq[Promptlet], separator: PromptletSeparator) {
  def render(state: State): String = {
    val styled               = promptlets.map(_.render(state)).filterNot(_.isEmpty)
    val separated            = new StringBuilder
    var previous: StyledText = null // scalafix:ok

    for (next <- styled) {
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

object PromptTheme {
  def apply(promptlets: Seq[Promptlet]): PromptTheme = apply(promptlets, PromptletSeparators.NoSeparator)
  def apply(promptlets: Seq[Promptlet], separator: (StyledText, StyledText) ⇒ StyledText): PromptTheme = {
    apply(promptlets, PromptletSeparator(separator))
  }
}
