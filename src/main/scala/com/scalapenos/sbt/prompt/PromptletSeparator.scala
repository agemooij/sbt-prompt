package com.scalapenos.sbt.prompt

/**
 * A PromptletSeparator is a function that takes two StyledText
 * instances, one before the separator and one after it, and produces
 * a StyledText instance that will be rendered in between the two
 * arguments.
 *
 * Feel free to ignore the arguments but you can do cool stuff by
 * combining the previous and next styles in some way.
 *
 * See the pre-packaged themes for some examples.
 */
case class PromptletSeparator(f: (StyledText, StyledText) ⇒ StyledText) {
  def render(previous: StyledText, next: StyledText) = f(previous, next)
}

trait PromptletSeparators extends Styles {
  val NoSeparator = PromptletSeparator((_, _) ⇒ StyledText.Empty)
}

object PromptletSeparators extends PromptletSeparators
