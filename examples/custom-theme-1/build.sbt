import com.scalapenos.sbt.prompt._
import SbtPrompt._
import autoImport._

lazy val customTheme1 = (project in file(".")).
  settings(
    name := "custom-theme-1",
    version := "0.1",
    description := "An example project to show off what you can do with sbt-prompt",
    startYear := Some(2014),
    homepage := Some(url("https://github.com/agemooij/sbt-prompt")),
    organization := "com.scalapenos",
    organizationHomepage := Some(url("http://scalapenos.com/")),
    licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT"))),
    promptTheme := PromptTheme(List(
        userName(fg(26)),
        text("@", fg(red)),
        hostName(fg(26)),
        text(" on ", fg(red)),
        gitBranch(clean = fg(green), dirty = fg(yellow)),
        text(" in ", fg(red)),
        currentProject(fg(magenta)),
        text(" ⇒ ", fg(red))
      ))
  )
