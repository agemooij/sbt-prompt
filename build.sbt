import sbt._
import scalariform.formatter.preferences._

lazy val root = project
  .withId("sbt-prompt")
  .in(file("."))
  .settings(
    sbtPlugin := true,
    crossSbtVersions := Vector("1.8.2"),

    libraryDependencies += {
      val currentSbtVersion = (sbtBinaryVersion in pluginCrossBuild).value
      Defaults.sbtPluginExtra("com.github.sbt" % "sbt-git" % "2.0.1", currentSbtVersion, scalaBinaryVersion.value)
    },

    libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25",

    version := "1.0.3-SNAPSHOT",
    organization := "com.scalapenos",

    description := "An SBT plugin for making your SBT prompt more awesome",
    startYear := Some(2014),
    homepage := Some(url("https://github.com/agemooij/sbt-prompt")),
    organizationHomepage := Some(url("https://github.com/agemooij/sbt-prompt")),

    scalacOptions := Seq("-encoding", "utf8", "-deprecation", "-Xlog-reflective-calls"),

    scalariformPreferences := scalariformPreferences.value
      .setPreference(AlignParameters, false)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 90)
      .setPreference(DoubleIndentConstructorArguments, true)
      .setPreference(DoubleIndentMethodDeclaration, true)
      .setPreference(RewriteArrowSymbols, true)
      .setPreference(DanglingCloseParenthesis, Preserve)
      .setPreference(NewlineAtEndOfFile, true)
      .setPreference(AllowParamGroupsOnNewlines, true)
  )
  .settings(
    publishMavenStyle := false,

    bintrayOrganization := None,
    bintrayRepository := "sbt-plugins",
    bintrayPackageLabels := Seq("sbt", "plugin", "prompt", "awesome"),

    licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
  )
