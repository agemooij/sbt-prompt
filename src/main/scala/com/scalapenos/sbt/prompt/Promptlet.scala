package com.scalapenos.sbt.prompt

import sbt._

/**
 * A Promptlet represents a function that can take the current SBT
 * project State to produce an instance of StyledText. This function
 * will be executed every time the Promptlet gets rendered.
 *
 * It also contains a mapText function and some shortcut transformers
 * for post-processing the text part of the resulting StyledText.
 */
case class Promptlet(content: State ⇒ StyledText) {
  def render(state: State): StyledText = content(state)
  def mapText(f: String ⇒ String)      = Promptlet(content.andThen(_.mapText(f)))

  def pad(padding: String)     = mapText(text ⇒ padding + text + padding)
  def padLeft(prefix: String)  = mapText(text ⇒ prefix + text)
  def padRight(suffix: String) = mapText(text ⇒ text + suffix)
}
