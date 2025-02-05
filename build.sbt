import sbt._
import org.typelevel.scalacoptions.ScalacOptions

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = project
  .withId("sbt-prompt")
  .in(file("."))
  .settings(
    sbtPlugin := true,
    crossSbtVersions := Vector("1.10.7"),

    libraryDependencies += {
      val currentSbtVersion = (pluginCrossBuild/sbtBinaryVersion).value
      Defaults.sbtPluginExtra("com.github.sbt" % "sbt-git" % "2.1.0", currentSbtVersion, scalaBinaryVersion.value)
    },

    libraryDependencies += "org.slf4j" % "slf4j-nop" % "2.0.16",

    version := "2.0.0-SNAPSHOT",
    

    description := "An SBT plugin for making your SBT prompt more awesome",
    startYear := Some(2014),
    homepage := Some(url("https://github.com/agemooij/sbt-prompt")),
    organizationHomepage := Some(url("https://github.com/agemooij/sbt-prompt")),

    scalacOptions := Seq("-encoding", "utf8", "-deprecation", "-Xlog-reflective-calls", "-Ywarn-unused-import"),

    semanticdbVersion := scalafixSemanticdb.revision,
    semanticdbEnabled := true,
    scalafmtOnCompile := true,
    scalafixOnCompile := true,

    tpolecatScalacOptions ++= Set(ScalacOptions.explain),
    Test / tpolecatExcludeOptions ++= Set(ScalacOptions.warnNonUnitStatement, ScalacOptions.warnValueDiscard),
    Test / console / tpolecatExcludeOptions ++= ScalacOptions.defaultConsoleExclude,
  )
  .settings(
    organization := "com.scalapenos",
    licenses := List("MIT" -> url("http://opensource.org/licenses/MIT")),
    homepage := Some(url("https://github.com/agemooij/sbt-prompt")),
  
    developers := List(
      Developer(
        "agemooij",
        "Age Mooij",
        "age.mooij@gmail.com",
        url("https://github.com/agemooij/")
      )
    )
  )
