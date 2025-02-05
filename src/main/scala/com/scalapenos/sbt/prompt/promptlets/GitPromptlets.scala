package com.scalapenos.sbt.prompt
package promptlets

import sbt._

import Keys._

trait GitPromptlets extends Styles {
  import com.github.sbt.git.SbtGit._

  def gitBranch(clean: Style = NoStyle, dirty: Style = NoStyle): Promptlet = gitPromptlet {
    case Some(git) ⇒ StyledText(git.branch, if (git.status.dirty) dirty else clean)
    case None      ⇒ StyledText.Empty
  }

  def gitPromptlet(render: Option[GitInfo] ⇒ StyledText): Promptlet = Promptlet(state ⇒ render(gitInfo(state)))

  case class GitInfo(branch: String, status: GitStatus)
  case class GitStatus(nrModified: Int, nrUntracked: Int) {
    val dirty = nrModified > 0 || nrUntracked > 0
  }

  // ==========================================================================
  // Implementation details
  // ==========================================================================

  private def gitInfo(state: State)(implicit extracted: Extracted = Project.extract(state)): Option[GitInfo] = {
    implicit val dir = extracted.get(baseDirectory)

    if (!isGitRepo(dir)) None
    else {
      Some(GitInfo(gitBranch(state), gitStatus(state)))
    }
  }

  private def gitBranch(state: State)(implicit dir: File, extracted: Extracted): String = {
    val reader           = extracted get GitKeys.gitReader
    val branchFromReader = reader.withGit(_.branch)

    if (branchFromReader != null) branchFromReader
    else {
      val (_, runner) = extracted.runTask(GitKeys.gitRunner, state)

      runner("symbolic-ref", "--short", "HEAD")(dir, NoOpSbtLogger)
    }
  }

  private def gitStatus(state: State)(implicit dir: File, extracted: Extracted): GitStatus = {
    def parseStatus(in: String) = in.split('\n').toVector.map(_.trim).filterNot(_.isEmpty).partition(!_.startsWith("?"))

    val (_, runner)           = extracted.runTask(GitKeys.gitRunner, state)
    val (modified, untracked) = parseStatus(runner("status", "--porcelain")(dir, NoOpSbtLogger))

    GitStatus(modified.size, untracked.size)
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
    def trace(t: ⇒ Throwable): Unit                      = {}
    def success(message: ⇒ String): Unit                 = {}
    def log(level: Level.Value, message: ⇒ String): Unit = {}
  }

  private object DebugSbtLogger extends Logger {
    def trace(t: ⇒ Throwable): Unit                      = {}
    def success(message: ⇒ String): Unit                 = { println(s"SUCCESS: ${message}.") }
    def log(level: Level.Value, message: ⇒ String): Unit = { println(s"LOG: ${message}.") }
  }
}

object GitPromptlets extends GitPromptlets
