**Note**: this README is still under construction but version 0.1 has now been released to Bintray and is waiting for inclusion in the central sbt plugin repository (Hi Josh! ;).

## Add that extra dash of awesome to your SBT shell prompt
If you're anything like me, you probably spent just as much, if not more,
time in the SBT shell as you do in your normal shell. So why not make it
look just as awesome?

This is the plugin for you if you would like to have a fancy SBT prompt
but you don't feel like spending the same insane amount of time as me
researching ANSI color escape codes and SBT internals. All you have to do
is pick one of the existing prompt themes or, if they're not awesome enough,
just use an easy mini-DSL to create your own custom theme.

Here's an example of the "Scalapenos" prompt theme, which I created to
exactly match my zsh prompt:

![Example screenshot](https://dl.dropboxusercontent.com/u/282610/sbt-prompt-example-screenshot.png "Example Screenshot")

That prompt consists of three **promptlets**:

- a label to indicate that we are in the SBT shell
- the current Git branch name and whether the branch is clean (green) or dirty (yellow)
- the current sbt project and, if aplicable, subproject.

Other available standard promptlets are:

- the current hostname
- the current username

All promptlets can be styled with foreground and background colors using
the ANSI-256 color space and you can customize them even further with prefixes,
suffixes, and other text transformers.


### Getting Started
You will need to be using SBT 0.13.5 or higher for this plugin to work correctly.
It was most extensively tested with SBT 0.13.6.

Add the following line to your plugins.sbt file:

    addSbtPlugin("com.scalapenos" % "sbt-prompt" % "0.1")

For normal build.sbt projects, this will automatically enable the
default prompt theme, which looks like this (green for a clean Git repo, yellow for a dirty one):

![Default theme](https://dl.dropboxusercontent.com/u/282610/sbt-prompt-default-theme.png "Default theme")

If you want to customize the theme, or if you are using a full Build.scala
(multi-)project build, you will need to add the following imports:

```scala
import com.scalapenos.sbt.prompt._
import SbtPrompt.autoImport._
```

If you want to use one of the existing themes, like the "Scalapenos theme"
show at the top of this README, just add the following setting to your build.sbt or build.scala:

```scala
promptTheme := PromptThemes.Scalapenos
```

Have a look at [the source code for ``PromptThemes``](https://github.com/agemooij/sbt-prompt/blob/master/src/main/scala/com/scalapenos/sbt/prompt/PromptThemes.scala) for the full list of built-in themes (a staggering 2 themes as of version 0.1) and how they are constructed by combining standard promptlets.


## Customization

### Available promptlets

- ``currentProject``: the current project name, including the current sub-project when applicable
- ``gitBranch``: the current Git branch name, with configurable styles for clean and dirty states
- ``hostName``: the current system hostname
- ``userName``: the current system username
- ``text``: for rendering everything else

All promptlets can be styled with foreground and background colors using
the ANSI-256 color space and you can customize them even further with prefixes,
suffixes, and other text transformers.

### Color Codes
The core assumption is that you're using

- [Ansi escape code reference](http://misc.flogisoft.com/bash/tip_colors_and_formatting)
- [Graphical overview of the Ansi 256 color codes](http://www.calmar.ws/vim/color-output.png)


### Examples


