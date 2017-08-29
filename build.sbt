import sbt._

import com.typesafe.sbt.SbtScalariform.ScalariformKeys._

lazy val root = project
  .copy(id = "sbt-prompt")
  .in(file("."))
  .settings(
    sbtPlugin := true,
    crossSbtVersions := Vector("0.13.16", "1.0.1"),

    libraryDependencies += {
      val currentSbtVersion = (sbtBinaryVersion in pluginCrossBuild).value
      Defaults.sbtPluginExtra("com.typesafe.sbt" % "sbt-git" % "0.9.3", currentSbtVersion, scalaBinaryVersion.value)
    },

    libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25",

    version := "1.0.2",
    organization := "com.scalapenos",

    description := Description,
    startYear := Some(2014),
    homepage := Some(url("https://github.com/agemooij/sbt-prompt")),
    organizationHomepage := Some(url("https://github.com/agemooij/sbt-prompt")),

    scalacOptions := Seq("-encoding", "utf8", "-deprecation", "-Xlog-reflective-calls"),

    preferences in Compile := ScalariformPreferences,
    preferences in Test    := ScalariformPreferences
  )
  .settings(lsSettings:_*)
  .settings(
    publishMavenStyle := false,

    bintrayOrganization := None,
    bintrayRepository := "sbt-plugins",
    bintrayPackageLabels := Labels,

    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),

    (LsKeys.tags in LsKeys.lsync) := Labels,
    (description in LsKeys.lsync) := Description
  )

lazy val Description = "An SBT plugin for making your SBT prompt more awesome"
lazy val Labels = Seq("sbt", "plugin", "prompt", "awesome")

lazy val ScalariformPreferences = {
  import scalariform.formatter.preferences._

  FormattingPreferences()
    .setPreference(AlignParameters, false)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 90)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(RewriteArrowSymbols, true)
    .setPreference(DanglingCloseParenthesis, Preserve)
}
