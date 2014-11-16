import bintray.Keys._
import scalariform.formatter.preferences._

name := "sbt-prompt"

version := "0.3-SNAPSHOT"

description := "An SBT plugin for making your SBT prompt more awesome"

startYear := Some(2014)

homepage := Some(url("https://github.com/agemooij/sbt-prompt"))

organization := "com.scalapenos"

organizationHomepage := Some(url("http://scalapenos.com/"))

licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT")))

sbtPlugin := true

scalacOptions := Seq("-deprecation", "-encoding", "utf8")

// Hard dependency. It should always be installed automatically together
// with sbt-prompt We cannot use plugin requirements yet because that only
// seems to work on other AutoPlugins and the sbt-git plugin is not one.
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.6.4")

// Code formating
scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
      .setPreference(AlignParameters, false)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 90)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(PreserveDanglingCloseParenthesis, true)
      .setPreference(RewriteArrowSymbols, true)

// Releasing
publishMavenStyle := false

seq(bintrayPublishSettings:_*)

repository in bintray := "sbt-plugins"

bintrayOrganization in bintray := None

packageLabels in bintray := Seq("sbt", "plugin", "prompt", "awesome")

seq(lsSettings:_*)

(LsKeys.tags in LsKeys.lsync) := Seq("sbt", "plugin", "prompt", "awesome")

(description in LsKeys.lsync) := "An SBT plugin for making your SBT prompt more awesome"
