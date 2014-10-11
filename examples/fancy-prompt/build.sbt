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
    promptTheme := PromptThemes.Default
  )
