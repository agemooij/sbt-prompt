package com.scalapenos.sbt.prompt

import sbt._
import Keys._

import promptlets._

object SbtPrompt extends AutoPlugin with Promptlets with Separators {
  object autoImport {
    val promptTheme = settingKey[PromptTheme]("A theme for rendering the SBT shell prompt.")
  }

  import autoImport._

  override def trigger = allRequirements

  override val projectSettings = Seq(
    promptTheme := PromptTheme(Seq(
      gitBranch(clean = fg(green), dirty = fg(yellow)).padLeft("[").padRight("] "),
      currentProject(fg(245)),
      text(": ", NoStyle)
    )),

    shellPrompt := (implicit state ⇒ promptTheme.value.render(state))
  )
}
