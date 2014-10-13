package com.scalapenos.sbt.prompt
package promptlets

import sbt._
import Keys._

/**
 * A collection of handy Promptlet factory methods.
 */
trait Promptlets extends BasicPromptlets with GitPromptlets

trait BasicPromptlets extends Styles {
  def text(content: String, style: Style): Promptlet = text(_ ⇒ content, style)
  def text(content: State ⇒ String, style: Style): Promptlet = Promptlet(state ⇒ StyledText(content(state), style))

  def currentProject(style: Style = NoStyle): Promptlet = Promptlet(state ⇒ {
    val extracted = Project.extract(state)
    val project = extracted.currentRef.project
    val root = extracted.rootProject(extracted.currentRef.build)

    StyledText(if (project == root) project else s"${root}/${project}", style)
  })

  def userName(style: Style = NoStyle): Promptlet = Promptlet(state ⇒ StyledText(cachedUserName, style))
  def hostName(style: Style = NoStyle): Promptlet = Promptlet(state ⇒ StyledText(cachedHostName, style))

  private lazy val cachedUserName = sys.props("user.name")
  private lazy val cachedHostName = java.net.InetAddress.getLocalHost().getHostName().stripSuffix(".local")
}
