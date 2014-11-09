import com.scalapenos.sbt.prompt._
import SbtPrompt._
import autoImport._

lazy val scalapenosTheme = (project in file(".")).
  settings(
    name := "scalapenos-theme",
    version := "0.1",
    description := """This project has been configured with the fancy Scalapenos theme, which
                      needs special font support to render correctly.
                      See the README for more details.""",
    startYear := Some(2014),
    homepage := Some(url("https://github.com/agemooij/sbt-prompt")),
    organization := "com.scalapenos",
    organizationHomepage := Some(url("http://scalapenos.com/")),
    promptTheme := PromptThemes.Scalapenos
    licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT"))),
  )
