![Version](https://img.shields.io/badge/version-1.0.0-brightgreen.svg?style=flat "1.0.0") ![License](https://img.shields.io/badge/license-MIT-blue.svg?style=flat "MIT")

## Add a dash of awesome to your SBT shell prompt
If you're anything like me, you probably spent just as much, if not more,
time in the SBT shell as you do in your normal shell. So why not make it
look just as awesome?

This is the plugin for you if you would like to have a fancy SBT prompt
but you don't feel like spending the same insane amount of time as me
researching ANSI color escape codes and SBT internals. All you have to do
is pick one of the existing prompt themes or, if they're not awesome enough,
just use an easy mini-DSL to create your own custom theme.

Here's an example of the "Scalapenos" prompt theme, which I created to
exactly match my zsh prompt (which is why this plugin was built):

![Example screenshot](https://dl.dropboxusercontent.com/u/282610/sbt-prompt-example-screenshot.png "Example Screenshot")

That example prompt consists of three **promptlets**:

- a label to indicate that we are in the SBT shell
- the current Git branch name and whether the branch is clean (green) or dirty (yellow)
- the current sbt project and, if applicable, subproject.

All promptlets can be styled with foreground and background colors and you can customize them even further with prefixes, suffixes, and other text transformers. See below for all customization options.

### Getting Started
You will need to be using SBT 0.13.5 or higher for this plugin to work correctly. It was created for SBT 0.13.6 and has been tested with every public release of SBT since (up to and including 0.13.11).

Add the following line to your plugins.sbt file (or `~/.sbt/0.13/plugins/plugin.sbt` to enable it for all projects):

```scala
addSbtPlugin("com.scalapenos" % "sbt-prompt" % "1.0.0")
```

For normal build.sbt projects, this will automatically enable the default prompt theme, which looks like this (green for a clean Git repo, yellow for a dirty one):

![Default theme](https://dl.dropboxusercontent.com/u/282610/sbt-prompt-default-theme.png "Default theme")

If you want to use one of the existing themes, like the "Scalapenos" theme shown at the top of this README, just add the following import and setting to your build.sbt file (or to any sbt file in `~/.sbt/0.13/`):

```scala
import com.scalapenos.sbt.prompt.SbtPrompt.autoImport._

promptTheme := com.scalapenos.sbt.prompt.PromptThemes.ScalapenosTheme
```

> **Note:** The Scalapenos theme uses some fancy unicode symbols as separators but these are not usually supported by the average console font. You will need to configure your terminal with [a Powerline-patched font](https://github.com/Lokaltog/powerline-fonts) or things will look a bit weird.

If you are using a Build.scala definition or when using custom themes, please add these alternative imports:

```scala
import com.scalapenos.sbt.prompt._
import SbtPrompt.autoImport._
```

The source code contains a number of small, self-contained example projects that show you how to [enable the plugin with the default theme](examples/default-theme/build.sbt), how to [enable the Scalapenos theme](examples/scalapenos-theme/build.sbt), and how to [configure a custom theme](examples/custom-theme-1/build.sbt).


## Customization
Writing your own prompt theme is prety easy. Let's start with some definitions.

### Definitions
A [**prompt theme**](src/main/scala/com/scalapenos/sbt/prompt/PromptTheme.scala) is a sequence of so-called **promptlets** that together represent the entire prompt.

A [**promptlet**](src/main/scala/com/scalapenos/sbt/prompt/Promptlet.scala) is a function that takes the [current project State](http://www.scala-sbt.org/0.13.6/api/#sbt.State) and produces some **styled text**.

[**Styled text**](src/main/scala/com/scalapenos/sbt/prompt/StyledText.scala) is some static text combined with a **style**.

A [**style**](src/main/scala/com/scalapenos/sbt/prompt/Styles.scala) is a combination of a foregound color and a background color with a little builder API for combining two colors into a style.

A [**color**](src/main/scala/com/scalapenos/sbt/prompt/Colors.scala) is a little wrapper around a color code from the ANSI 256 color space (i.e. a number from 0 up to and including 255) plus the ability to produce the correct escape codes for rendering themselves to any terminal configured to emulate *xterm-256color* (or one compatible with it). There is a handy list of links to color references at the bottom of this README.

Advanced prompt themes, like the builtin Scalapenos theme, might want to create some kind of styled transition between promptlets based on their separate styles. To enable this, a prompt theme can be configured with an optional [**separator**](src/main/scala/com/scalapenos/sbt/prompt/PromptletSeparator.scala), which is a function that takes the styled output of two promptlets to produce some styled text that will be rendered in between those two promptlets.


### Available promptlets
The list of builtin promptlets is relatively small but it should cover all the basics.

#### Promplet: ``currentProject(style)``
Renders the current project name, including the current sub-project when applicable. Example:

```scala
currentProject(fg(green))
```

#### Promplet: ``gitBranch(cleanStyle, dirtyStyle)``
Renders the current Git branch name, with configurable styles for clean and dirty states. Example:

```scala
gitBranch(clean = fg(green), dirty = fg(red))
```

If the current project is not a Git repository, this promptlet will produce ``StyledText.Empty``.

#### Promplet: ``gitPromptlet(render: Option[GitInfo] ⇒ StyledText)``
The more powerful form of ``gitBranch``, which lets you use more specific Git status information to produce some styled text. If the current project is not a Git repository, the ``Option[GitInfo]`` parameter to the ``render`` function will be ``None``.

The available Git information is encoded in these two case classes:

```scala
case class GitInfo(branch: String, status: GitStatus)
case class GitStatus(nrModified: Int, nrUntracked: Int) {
  val dirty = nrModified > 0 || nrUntracked > 0
}
```

The ``gitBranch`` promptlet is built using ``gitPromptlet``:

```scala
def gitBranch(clean: Style = NoStyle, dirty: Style = NoStyle): Promptlet = gitPromptlet {
  case Some(git) ⇒ StyledText(git.branch, if (git.status.dirty) dirty else clean)
  case None      ⇒ StyledText.Empty
}
```

#### Promptlet: ``currentScalaVersion(style)``
Renders the current scala version in the project. Example:

```scala
currentScalaVersion(fg(red))
```

#### Promplet: ``hostName(style)``
Renders the current system hostname. Example:

```scala
hostName(fg(blue))
```

#### Promplet: ``userName(style)``
Renders your current system username. Example:

```scala
userName(fg(white).bg(blue))
```

#### Promplet: ``text(text: String, style)``
Renders the specified static text. Example:

```scala
text(fg(235).bg(26)) // using raw ANSI 256 color codes
```

#### Promptlet: ``text(text: State ⇒ String, style)``
Allows you to extract whatever information you want from the [current project State](http://www.scala-sbt.org/0.13.6/api/#sbt.State) and render it as dynamic text every time the prompt gets re-drawn.

As an example, this is how the ``projectName`` promptlet is implemented:

```scala
def currentProject(style: Style = NoStyle): Promptlet = text(state ⇒ {
  val extracted = Project.extract(state)
  val project = extracted.currentRef.project
  val root = extracted.rootProject(extracted.currentRef.build)

  if (project == root) project else s"${root}/${project}"
}, style)
```

#### Post-processing styled text with ``mapText``
Sometimes you want to take an existing promptlet like ``gitBranch`` and add some extra text to it in the same style. Enter the ``mapText(String ⇒ String)`` function, available on all promptlets, which allows you to post-process the raw text any way you want. For your convenience some common post-processors like ``padLeft`` and ``padRight`` have alrady been defined.

Example:
```scala
gitBranch(clean = fg(green), dirty = fg(yellow)).mapText(_.toUpperCase).padLeft("[").padRight("]")
```

### Combining promptlets into a theme

To define a theme you simply call its apply method with a sequence of promptlets and an optional separator. Here's a few examples:

#### The default theme
```scala
PromptTheme(List(
  gitBranch(clean = fg(green), dirty = fg(yellow)).padLeft("[").padRight("] "),
  currentProject(),
  text(": ", NoStyle)
))
```

#### A custom theme
```scala
PromptTheme(List(
  text("[SBT] ", fg(green)),
  userName(fg(26)),
  text("@", fg(green)),
  hostName(fg(26)),
  text(" on ", fg(red)),
  gitBranch(clean = fg(green), dirty = fg(yellow)),
  text(" in ", fg(red)),
  currentProject(fg(magenta)),
  text(" ⇒  ", fg(red))
))
```

#### The Scalapenos theme
The Scalapenos theme uses a separator and some fancy unicode symbols that are not supported by your average font. You will need to configure your terminal with [a Powerline-patched font](https://github.com/Lokaltog/powerline-fonts).

```scala
PromptTheme(
  List(
    text(" SBT ", fg(235).bg(26)),
    gitBranch(clean = fg(235).bg(34), dirty = fg(235).bg(214)).padLeft("  ").padRight(" "),
    currentProject(fg(250).bg(235)).pad(" "),
    text(" ", NoStyle)
  ),
  (previous, next) ⇒ StyledText("", fg(previous.style.background).bg(next.style.background))
)
```

### Examples
- How to [enable the plugin with the default theme](examples/default-theme/build.sbt)
- How to [enable the Scalapenos theme](examples/scalapenos-theme/build.sbt)
- How to [configure a custom theme](examples/custom-theme-1/build.sbt)


### Questions, issues, pull requests, cool themes, etc.
Yes please!


### Color references
- [Graphical overview of the Ansi 256 color codes](http://www.calmar.ws/vim/color-output.png)
- [Ansi escape code reference](http://misc.flogisoft.com/bash/tip_colors_and_formatting)


## License
This plugin is licensed under the [MIT license]("http://opensource.org/licenses/MIT").
