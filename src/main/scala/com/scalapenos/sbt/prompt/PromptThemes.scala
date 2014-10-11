package com.scalapenos.sbt.prompt

import sbt._
import Keys._

import promptlets._

object PromptThemes extends Promptlets with PromptletSeparators {
  /**
   * A minimalistic theme that will show:
   *
   *  - the current Git branch (in green for a clean working directory, in yellow for a dirty one)
   *  - the current project, including the root project of a multi-project build
   *
   * This theme is the default theme
   */
  val Default = PromptTheme(List(
    gitBranch(clean = fg(green), dirty = fg(yellow)).padLeft("[").padRight("] "),
    currentProject(),
    text(": ", NoStyle)
  ))

  /**
   * An advanced theme that uses a combination of fancy colors and unicode separators
   * to achieve a very polished effect.
   *
   * This prompt will show the following information:
   *
   *  - a handy marker to indicate you are in SBT (instead of a normal bash/zsh shell)
   *  - the current Git branch (in green for a clean working directory, in yellow for a dirty one)
   *  - the current project, including the root project of a multi-project build
   *
   * Note:
   *  This theme uses some special icon-like characters (the separator character
   *  and the Git branch icon) that are not supported by most fonts. In order
   *  for this theme to render correctly, you will need to configure your terminal
   *  with a Powerline-patched font.
   *  See https://github.com/Lokaltog/powerline-fonts
   *
   * This theme is based on a customized version of the "agnoster" oh-my-zsh theme.
   * More information here:
   *  - https://gist.github.com/agnoster/3712874
   *  - https://github.com/robbyrussell/oh-my-zsh/blob/master/themes/agnoster.zsh-theme
   */
  val Scalapenos = PromptTheme(
    List(
      text(" SBT ", fg(235).bg(26)),
      gitBranch(clean = fg(235).bg(34), dirty = fg(235).bg(214)).padLeft("  ").padRight(" "),
      currentProject(fg(250).bg(235)).pad(" "),
      text(" ", NoStyle)
    ),
    (previous, next) ⇒ StyledText("", fg(previous.style.background).bg(next.style.background))
  )
}
