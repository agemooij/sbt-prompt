package com.scalapenos.sbt.prompt

import sbt._
import Keys._

/**
 * A PromptTheme contains a sequence of Promptlets and an optional Separator.
 * It can render itself into a prompt string based on the current SBT State.
 */
case class PromptTheme(promptlets: Seq[Promptlet], separator: PromptletSeparator = PromptletSeparators.NoSeparator) {
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
