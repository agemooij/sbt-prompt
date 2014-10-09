import scalariform.formatter.preferences._

name := "sbt-prompt"

organization := "com.scalapenos"

version := "0.1"

description := "An SBT plugin for making your SBT prompt more awesome"

startYear := Some(2014)

homepage := Some(url("https://github.com/agemooij/sbt-prompt"))

organizationHomepage := Some(url("http://scalapenos.com/"))

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

sbtPlugin := true

scalacOptions := Seq("-deprecation", "-encoding", "utf8")

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.6.4")

scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
      .setPreference(AlignParameters, false)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 90)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(PreserveDanglingCloseParenthesis, true)
      .setPreference(RewriteArrowSymbols, true)
