package com.scalapenos.sbt.prompt

import sbt._
import Keys._

import promptlets._

object SbtPrompt extends AutoPlugin {
  object autoImport extends Promptlets with PromptletSeparators with PromptThemes {
    val promptTheme = settingKey[PromptTheme]("A theme for rendering the SBT shell prompt.")
  }

  import autoImport._

  override def trigger = allRequirements

  override val projectSettings = Seq(
    /** Sets the default theme. */
    promptTheme := PromptThemes.Default,

    /**
     * Sets the SBT shell prompt to a function that renders the configured prompt theme.
     */
    shellPrompt := (implicit state ⇒ promptTheme.value.render(state))
  )
}
