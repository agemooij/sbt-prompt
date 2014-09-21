package com.scalapenos

import sbt._
import Keys._

object SbtPrompt extends AutoPlugin with Segments with Separators {
  object autoImport {
    val promptSegments = settingKey[Seq[Segment]]("A sequence of prompt segments that will be used to build the SBT prompt.")
    val promptSeparator = settingKey[Separator]("The separator that will be inserted between two prompt segments.")
  }

  import autoImport._

  override def trigger = allRequirements

  override val projectSettings = Seq(
    promptSeparator := NoSeparator,
    promptSegments := Seq(
      gitBranch(clean = green, dirty = yellow).padLeft("[").padRight("] "),
      currentProject(fg(245)),
      text(": ", NoStyle)
    ),

    shellPrompt := (implicit state => Prompt(promptSegments.value, promptSeparator.value).render(state))
  )
}
