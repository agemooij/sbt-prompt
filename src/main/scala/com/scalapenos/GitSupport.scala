package com.scalapenos

import sbt._
import Keys._

object GitSupport {
  import com.typesafe.sbt.SbtGit._

  def currentBranch(state: State)(implicit extracted: Extracted = Project.extract(state)): String = {
    val reader = extracted get GitKeys.gitReader

    reader.withGit(_.branch)
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
