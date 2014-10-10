import com.scalapenos.sbt.prompt._
import SbtPrompt._
import autoImport._

lazy val fancyPrompt = (project in file(".")).
  settings(
    name := "fancy-prompt",
    version := "0.1",
    description := "An example project to show off what you can do with sbt-prompt",
    startYear := Some(2014),
    homepage := Some(url("https://github.com/agemooij/sbt-prompt")),
    organization := "com.scalapenos",
    organizationHomepage := Some(url("http://scalapenos.com/")),
    licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    promptTheme := PromptTheme(List(
      gitBranch(clean = fg(green), dirty = fg(yellow)).padLeft("[").padRight("] "),
      currentProject(fg(245)),
      text(": ", NoStyle)
    ))
    // promptTheme := PromptTheme(
    //   Seq(
    //     text(" SBT ", fg(235).bg(26)),
    //     gitBranch(clean = fg(235).bg(34), dirty = fg(235).bg(214)).padLeft("  ").padRight(" "),
    //     currentProject(fg(250).bg(235)).pad(" "),
    //     text(" ", NoStyle)
    //   ),
    //   (previous, next) => StyledText("", fg(previous.style.background).bg(next.style.background))
    // )
  )
