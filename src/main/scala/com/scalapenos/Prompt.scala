package com.scalapenos

import sbt._
import Keys._

case class Prompt(segments: Seq[Segment], separator: Separator = Separators.NoSeparator) {
  def render(state: State): String = {
    val styled = segments.map(_.render(state))
    var separated = new collection.mutable.ArrayBuffer[StyledText]((styled.size * 2) - 1)
    var previous: StyledText = null

    for (next ← styled) {
      if (previous == null) {
        separated += next
      } else {
        separated += StyledText(separator.text, separator.style(previous.style, next.style))
        separated += next
      }

      previous = next
    }

    // TODO: inline the mkString part by using our own StringBuilder instead of first creating a new collection
    separated.map(_.rendered).mkString("")
  }
}

case class Separator(text: String, style: (Style, Style) ⇒ Style)

trait Separators extends Styles {
  val NoSeparator = Separator("", (_, _) ⇒ NoStyle)
}
object Separators extends Separators

case class Segment(content: State ⇒ StyledText) {
  def render(state: State): StyledText = content(state)

  def mapText(f: String ⇒ String) = Segment(content.andThen(_.mapText(f)))

  def pad(padding: String) = mapText(text ⇒ padding + text + padding)
  def padLeft(prefix: String) = mapText(text ⇒ prefix + text)
  def padRight(suffix: String) = mapText(text ⇒ text + suffix)
}

trait Segments extends Styles {
  def text(content: String, style: Style): Segment = text(_ ⇒ content, style)
  def text(content: State ⇒ String, style: Style): Segment = Segment(state ⇒ StyledText(content(state), style))

  def currentProject(style: Style = NoStyle) = Segment(state ⇒ {
    val extracted = Project.extract(state)
    val project = extracted.currentRef.project
    val root = extracted.rootProject(extracted.currentRef.build)

    StyledText(if (project == root) project else s"${root}/${project}", style)
  })

  def gitBranch(clean: Style = NoStyle, dirty: Style = NoStyle) = Segment(state ⇒ {
    GitSupport.gitInfo(state) match {
      case Some(git) if git.dirty ⇒ StyledText(git.branch, dirty)
      case Some(git)              ⇒ StyledText(git.branch, clean)
      case None                   ⇒ StyledText("")
    }
  })
}
