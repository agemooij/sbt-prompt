package com.scalapenos

import sbt._
import Keys._

case class PromptTheme(promptlets: Seq[Promptlet], separator: Separator = Separators.NoSeparator) {
  def render(state: State): String = {
    val styled = promptlets.map(_.render(state)).filterNot(_.isEmpty)
    val separated = new StringBuilder
    var previous: StyledText = null

    for (next ← styled) {
      if (previous == null) {
        separated ++= next.rendered
      } else {
        separated ++= separator.render(previous, next).rendered
        separated ++= next.rendered
      }

      previous = next
    }

    separated.toString
  }
}

case class Separator(text: String, styleCombiner: (Style, Style) ⇒ Style) {
  def render(previous: StyledText, next: StyledText) = StyledText(text, styleCombiner(previous.style, next.style))
}

trait Separators extends Styles {
  val NoSeparator = Separator("", (_, _) ⇒ NoStyle)
}
object Separators extends Separators

case class Promptlet(content: State ⇒ StyledText) {
  def render(state: State): StyledText = content(state)
  def mapText(f: String ⇒ String) = Promptlet(content.andThen(_.mapText(f)))

  def pad(padding: String) = mapText(text ⇒ padding + text + padding)
  def padLeft(prefix: String) = mapText(text ⇒ prefix + text)
  def padRight(suffix: String) = mapText(text ⇒ text + suffix)
}

trait Promptlets extends Styles {
  def text(content: String, style: Style): Promptlet = text(_ ⇒ content, style)
  def text(content: State ⇒ String, style: Style): Promptlet = Promptlet(state ⇒ StyledText(content(state), style))

  def currentProject(style: Style = NoStyle): Promptlet = Promptlet(state ⇒ {
    val extracted = Project.extract(state)
    val project = extracted.currentRef.project
    val root = extracted.rootProject(extracted.currentRef.build)

    StyledText(if (project == root) project else s"${root}/${project}", style)
  })

  def gitBranch(clean: Style = NoStyle, dirty: Style = NoStyle): Promptlet = Promptlet(state ⇒ {
    GitSupport.gitInfo(state) match {
      case Some(git) if git.dirty ⇒ StyledText(git.branch, dirty)
      case Some(git)              ⇒ StyledText(git.branch, clean)
      case None                   ⇒ StyledText.Empty
    }
  })
}
