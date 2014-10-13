import com.scalapenos.sbt.prompt._
import SbtPrompt._
import autoImport._

lazy val defaultTheme = (project in file(".")).
  settings(
    name := "default-theme",
    version := "0.1",
    description := "Without any configuration, sbt-prompt will use the default theme.",
    startYear := Some(2014),
    homepage := Some(url("https://github.com/agemooij/sbt-prompt")),
    organization := "com.scalapenos",
    organizationHomepage := Some(url("http://scalapenos.com/")),
    licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
  )
