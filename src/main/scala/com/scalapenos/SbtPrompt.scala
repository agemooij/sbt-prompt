package com.scalapenos

import sbt._
import Keys._

object SbtPrompt extends AutoPlugin {
  object autoImport {
    // val promptSegments = settingKey[Seq[PromptSegment]]("A sequence of prompt segments that will be used to build the SBT prompt.")
    // val promptSegmentSeparator = settingKey[PromptSegmentSeparator]("The separator that will be inserted between two prompt segments.")
  }

  import autoImport._

  override def trigger = allRequirements

  override val projectSettings = Seq(
    // promptSegments := {
    //   Seq(
    //     textLabel("SBT", Color(26, 235)), // gray on blue
    //     gitBranch(clean = Color(34, 235), dirty = Color(214, 235)),
    //     currentProject(Color(235, 250))
    //   )
    // }

    // promptSegmentSeparator := {
    //   separator("")((before, after) => Color(after.bg, before.bg)),
    // }



    shellPrompt := { implicit state =>
      import Segment._
      import Style._

      List(
        text(fg(235).bg(26), " SBT "),
        text(fg(26).bg(34), ""), // dynamic: fg = previous.bg, bg = next.bg
        text(fg(235).bg(34), " "), // dynamic: bg = next.bg, fg = next.fg
        gitBranch(dirty = fg(235).bg(34), clean = fg(235).bg(34)),
        text(fg(34).bg(235), ""), // dynamic: fg = previous.bg, bg = next.bg
        currentProject(fg(250).bg(235)),
        text(fg(235), ""), // dynamic: fg = previous.bg
        text(Defaults, " ")
      ).map(_.render.toString).mkString("")


      // Combo(
      //   Combo(
      //     Combo(
      //       text(fg(235).bg(26), " SBT "),
      //       gitBranch(dirty = fg(235).bg(34), clean = fg(235).bg(34))
      //     ) separatedBy { (previous, next) =>
      //       text(fg(previous.style.bg).bg(next.style.bg), "") +
      //       text(fg(next.style.fg).bg(next.style.bg), " ")
      //     },
      //     currentProject(fg(250).bg(235))
      //   ) separatedBy { (previous, next) =>
      //     text(fg(previous.style.bg).bg(next.style.bg), "")
      //   },
      //   text(Defaults, " ")
      // ) separatedBy { (previous, next) =>
      //   text(fg(previous.style.bg).bg(next.style.bg), "")
      // }.render.toString



      // Combo(
      //   Combo(
      //     Combo(
      //       text(fg(235).bg(26), " SBT "),
      //       (previous, next) => text(fg(previous.style.bg).bg(next.style.bg), "") + text(fg(next.style.fg).bg(next.style.bg), " ")
      //       gitBranch(dirty = fg(235).bg(34), clean = fg(235).bg(34))
      //     ),
      //     (previous, next) => text(fg(previous.style.bg).bg(next.style.bg), "")
      //     currentProject(fg(250).bg(235))
      //   ),
      //   (previous, next) => text(fg(previous.style.bg).bg(next.style.bg), "")
      //   text(Defaults, " ")
      // ).render.toString




    }
  )
}


// list of segments
// render => list of StyledTexts
//
// (previous: StyledText, next: StyledText) => StyledText
// (previous: Style, next: Style) => StyledText
// (previous: Style, next: Style) => Style
//
//


case class StyledText(style: Style, text: String) {
  lazy val rendered = style.render(text)
  override def toString = rendered
}

case class Segment(content: State => StyledText) {
  def render(implicit state: State): StyledText = content(state)
}

object Segment {
  def text(style: Style, content: String): Segment = text(style, _ => content)
  def text(style: Style, content: State => String): Segment = Segment(state => StyledText(style, content(state)))

  def currentProject(style: Style) = Segment( state => {
    val extracted = Project.extract(state)
    val project = extracted.currentRef.project
    val root = extracted.rootProject(extracted.currentRef.build)

    StyledText(style, if (project == root) s" ${project} " else s" ${root}/${project} ")
  })

  def gitBranch(clean: Style, dirty: Style) = Segment( state => {
    val branch = s" ${GitSupport.currentBranch(state)} "

    if (GitSupport.isWorkingCopyDirty(state)) StyledText(dirty, branch) else StyledText(clean, branch)
  })
}

// class Separator extends (Segment, Segment) => Segment


object GitSupport {
  import com.typesafe.sbt.SbtGit._

  def currentBranch(state: State)(implicit extracted: Extracted = Project.extract(state)): String = {
    extracted.get(GitKeys.gitCurrentBranch)
  }

  def isWorkingCopyDirty(state: State)(implicit extracted: Extracted = Project.extract(state)): Boolean = {
    val (_, runner) = extracted.runTask(GitKeys.gitRunner, state)
    val dir = extracted.get(baseDirectory)
    val result = runner("diff-index", "HEAD", "--")(dir, NoOpSbtLogger)

    !result.trim.isEmpty
  }

  private object NoOpSbtLogger extends Logger {
    def trace(t: => Throwable): Unit = {}
    def success(message: => String): Unit = {}
    def log(level: Level.Value, message: => String): Unit = {}
  }
}





case class Color(code: Int) {
  require((code >= 0 && code <= 255) || code == Color.DefaultCode)

  def bg = if (code == Color.DefaultCode) "\u001b[49m" else s"\u001b[48;5;${code}m"
  def fg = if (code == Color.DefaultCode) "\u001b[39m" else s"\u001b[38;5;${code}m"
}

object Color {
  val DefaultCode = -42
  val Default = Color(DefaultCode)

  val red = Color(9)
  val green = Color(10)
  val yellow = Color(11)
  val blue = Color(12)
  val magenta = Color(13)
  val cyan = Color(14)
  val white = Color(15)
}

case class Style(background: Color = Color.Default, foreground: Color = Color.Default) {
  def render(text: String) = s"${background.bg}${foreground.fg}${text}${Color.Default.bg}${Color.Default.fg}"

  def bg(color: Color) = copy(background = color)
  def bg(color: Int) = copy(background = Color(color))

  def fg(color: Color) = copy(foreground = color)
  def fg(color: Int) = copy(foreground = Color(color))

  def red = fg(Color.red)
  def green = fg(Color.green)
  def yellow = fg(Color.yellow)
  def blue = fg(Color.blue)
  def magenta = fg(Color.magenta)
  def cyan = fg(Color.cyan)
  def white = fg(Color.white)

  def bgRed = bg(Color.red)
  def bgGreen = bg(Color.green)
  def bgYellow = bg(Color.yellow)
  def bgBlue = bg(Color.blue)
  def bgMagenta = bg(Color.magenta)
  def bgCyan = bg(Color.cyan)
  def bgWhite = bg(Color.white)
}

object Style {
  val Defaults = Style()

  def bg(color: Color) = Style(background = color)
  def bg(color: Int) = Style(background = Color(color))

  def fg(color: Color) = Style(foreground = color)
  def fg(color: Int) = Style(foreground = Color(color))

  val red = fg(Color.red)
  val green = fg(Color.green)
  val yellow = fg(Color.yellow)
  val blue = fg(Color.blue)
  val magenta = fg(Color.magenta)
  val cyan = fg(Color.cyan)
  val white = fg(Color.white)

  val bgRed = bg(Color.red)
  val bgGreen = bg(Color.green)
  val bgYellow = bg(Color.yellow)
  val bgBlue = bg(Color.blue)
  val bgMagenta = bg(Color.magenta)
  val bgCyan = bg(Color.cyan)
  val bgWhite = bg(Color.white)
}
