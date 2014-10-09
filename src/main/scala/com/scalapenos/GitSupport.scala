package com.scalapenos

import sbt._
import Keys._

object GitSupport {
  import com.typesafe.sbt.SbtGit._

  case class GitInfo(branch: String, dirty: Boolean)

  def gitInfo(state: State)(implicit extracted: Extracted = Project.extract(state)): Option[GitInfo] = {
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

      // runner("rev-parse", "--abbrev-ref", "HEAD")(dir, NoOpSbtLogger)
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
    def success(message: ⇒ String): Unit = { println(s"SUCCESS: ${message}.") }
    def log(level: Level.Value, message: ⇒ String): Unit = { println(s"LOG: ${message}.") }
  }
}
