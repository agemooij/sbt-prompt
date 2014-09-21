package com.scalapenos

import sbt._
import Keys._

object SbtPrompt extends AutoPlugin {
  object autoImport {
    // val promptSegments = settingKey[Seq[PromptSegment]]("A sequence of prompt segments that will be used to build the SBT prompt.")
    // val promptSeparator = settingKey[PromptSeparator]("The separator that will be inserted between two prompt segments.")
  }

  import autoImport._

  override def trigger = allRequirements

  override val projectSettings = Seq(
    // promptSegments := {
    //   Seq(
    //     textLabel("SBT", Color(26, 235)), // gray on blue
    //     gitBranch(clean = Color(34, 235), dirty = Color(214, 235)),
    //     currentProject(Color(235, 250))
    //   )
    // }

    // promptSegmentSeparator := {
    //   separator("")((before, after) => Color(after.bg, before.bg)),
    // }



    shellPrompt := { implicit state =>
      import Segment._
      import Style._

      val prompt = Prompt(
        List(
          text(" SBT ", fg(235).bg(26)),
          gitBranch(clean = fg(235).bg(34), dirty = fg(235).bg(214)).withPrefix(" "),
          currentProject(fg(250).bg(235)),
          text(" ", Defaults)
        ),
        Separator("", (previous, next) => fg(previous.background).bg(next.background))
      )

      prompt.render(state)
    }
  )
}
