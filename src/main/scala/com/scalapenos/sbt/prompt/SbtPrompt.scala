package com.scalapenos.sbt.prompt

import sbt._
import Keys._

import promptlets._

object SbtPrompt extends AutoPlugin with Promptlets with PromptletSeparators {
  object autoImport {
    val promptTheme = settingKey[PromptTheme]("A theme for rendering the SBT shell prompt.")
  }

  import autoImport._

  override def trigger = allRequirements

  override val projectSettings = Seq(
    /** The default theme. */
    promptTheme := PromptTheme(Seq(
      gitBranch(clean = fg(green), dirty = fg(yellow)).padLeft("[").padRight("] "),
      currentProject(),
      text(": ", NoStyle)
    )),

    /**
     * Sets the SBT shell prompt to a function that renders the configured prompt theme.
     */
    shellPrompt := (implicit state ⇒ promptTheme.value.render(state))
  )
}
