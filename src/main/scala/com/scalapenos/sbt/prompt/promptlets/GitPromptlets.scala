package com.scalapenos.sbt.prompt
package promptlets

import sbt._
import Keys._

trait GitPromptlets extends Styles {
  import com.typesafe.sbt.SbtGit._

  def gitBranch(clean: Style = NoStyle, dirty: Style = NoStyle): Promptlet = Promptlet(state ⇒ {
    gitInfo(state) match {
      case Some(git) if git.dirty ⇒ StyledText(git.branch, dirty)
      case Some(git)              ⇒ StyledText(git.branch, clean)
      case None                   ⇒ StyledText.Empty
    }
  })

  private case class GitInfo(branch: String, dirty: Boolean)

  private def gitInfo(state: State)(implicit extracted: Extracted = Project.extract(state)): Option[GitInfo] = {
    implicit val dir = extracted.get(baseDirectory)

    if (!isGitRepo(dir)) None
    else {
      Some(GitInfo(currentBranch(state), isWorkingCopyDirty(state)))
    }
  }

  private def currentBranch(state: State)(implicit dir: File, extracted: Extracted): String = {
    val reader = extracted get GitKeys.gitReader
    val branchFromReader = reader.withGit(_.branch)

    if (branchFromReader != null) branchFromReader
    else {
      val (_, runner) = extracted.runTask(GitKeys.gitRunner, state)

      runner("symbolic-ref", "--short", "HEAD")(dir, NoOpSbtLogger)
    }
  }

  private def isWorkingCopyDirty(state: State)(implicit dir: File, extracted: Extracted): Boolean = {
    val (_, runner) = extracted.runTask(GitKeys.gitRunner, state)
    val result = runner("diff-index", "HEAD", "--")(dir, NoOpSbtLogger)

    !result.trim.isEmpty
  }

  @scala.annotation.tailrec
  private def isGitRepo(dir: File): Boolean = {
    if (dir.listFiles().map(_.getName).contains(".git")) true
    else {
      val parent = dir.getParentFile
      if (parent == null) false
      else isGitRepo(parent)
    }
  }

  private object NoOpSbtLogger extends Logger {
    def trace(t: ⇒ Throwable): Unit = {}
    def success(message: ⇒ String): Unit = {}
    def log(level: Level.Value, message: ⇒ String): Unit = {}
  }

  private object DebugSbtLogger extends Logger {
    def trace(t: ⇒ Throwable): Unit = {}
    def success(message: ⇒ String): Unit = { println(s"SUCCESS: ${message}.") }
    def log(level: Level.Value, message: ⇒ String): Unit = { println(s"LOG: ${message}.") }
  }
}

object GitPromptlets extends GitPromptlets
