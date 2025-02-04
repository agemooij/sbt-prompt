package com.scalapenos.sbt.prompt
package promptlets

import sbt._

import Keys._

/**
 * A collection of handy Promptlet factory methods.
 */
trait Promptlets extends BasicPromptlets with GitPromptlets

trait BasicPromptlets extends Styles {
  def text(content: String, style: Style): Promptlet         = text(_ ⇒ content, style)
  def text(content: State ⇒ String, style: Style): Promptlet = Promptlet(state ⇒ StyledText(content(state), style))

  def currentProject(style: Style = NoStyle): Promptlet = text(
    state ⇒ {
      val extracted = Project.extract(state)
      val project   = extracted.currentRef.project
      val root      = extracted.rootProject(extracted.currentRef.build)

      if (project == root) project else s"${root}/${project}"
    },
    style
  )

  def userName(style: Style = NoStyle): Promptlet = text(_ ⇒ cachedUserName, style)
  def hostName(style: Style = NoStyle): Promptlet = text(_ ⇒ cachedHostName, style)

  def currentSbtKey(settingKey: SettingKey[_], style: Style = NoStyle): Promptlet = text(
    state ⇒ {
      val extracted = Project.extract(state)
      s"${extracted.get(settingKey)}"
    },
    style
  )

  def currentScalaVersion(style: Style = NoStyle) =
    currentSbtKey(scalaVersion, style)

  def currentSbtVersion(style: Style = NoStyle) =
    currentSbtKey(sbtVersion, style)

  private lazy val cachedUserName = sys.props("user.name")
  private lazy val cachedHostName = java.net.InetAddress.getLocalHost().getHostName().stripSuffix(".local")
}
