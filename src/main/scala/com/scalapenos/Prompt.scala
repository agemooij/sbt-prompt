package com.scalapenos

import sbt._
import Keys._


case class Prompt(segments: Seq[Segment], separator: Separator) {
  def render(state: State): String = {
    val styled = segments.map(_.render(state))
    var separated = new collection.mutable.ArrayBuffer[StyledText]((styled.size * 2) - 1)
    var previous: StyledText = null

    for (next <- styled) {
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


case class Separator(text: String, style: (Style, Style) => Style)

case class Segment(content: State => StyledText) {
  def render(state: State): StyledText = content(state)
  def withPrefix(prefix: String) = Segment(content.andThen(_.withPrefix(prefix)))
  def withSuffix(suffix: String) = Segment(content.andThen(_.withSuffix(suffix)))
}

object Segment {
  def text(content: String, style: Style): Segment = text(_ => content, style)
  def text(content: State => String, style: Style): Segment = Segment(state => StyledText(content(state), style))

  def currentProject(style: Style) = Segment( state => {
    val extracted = Project.extract(state)
    val project = extracted.currentRef.project
    val root = extracted.rootProject(extracted.currentRef.build)

    StyledText(if (project == root) s" ${project} " else s" ${root}/${project} ", style)
  })

  def gitBranch(clean: Style, dirty: Style) = Segment( state => {
    val branch = s" ${GitSupport.currentBranch(state)} "

    if (GitSupport.isWorkingCopyDirty(state)) StyledText(branch, dirty)
    else StyledText(branch, clean)
  })
}
