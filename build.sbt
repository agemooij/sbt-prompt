import bintray.Keys._
import scalariform.formatter.preferences._

name := "sbt-prompt"

version := "0.1"

description := "An SBT plugin for making your SBT prompt more awesome"

startYear := Some(2014)

homepage := Some(url("https://github.com/agemooij/sbt-prompt"))

organization := "com.scalapenos"

organizationHomepage := Some(url("http://scalapenos.com/"))

licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT")))

sbtPlugin := true

scalacOptions := Seq("-deprecation", "-encoding", "utf8")

// Hard dependency. It should always be installed automatically together with sbt-prompt
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



