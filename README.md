## A plugin that adds that extra dash of awesome to your SBT shell prompt
If you're anything like me, you probably spent just as much, if not more,
time in the SBT shell as you do in your normal shell. So why not make it
look just as awesome?

This is the plugin for you if you would like to have a fancy SBT prompt
but you don't feel like spending the same insane amount of time as me
researching ANSI color escape codes and SBT internals. All you have to do
is pick one of the existing prompt themes or, if they're not awesome enopugh,
use an easy little min-DSL to create your own custom theme.

Here's an example of the "Scalapenos" prompt theme, which I created to
exactly match my zsh prompt:

![Example screenshot](https://dl.dropboxusercontent.com/u/282610/sbt-prompt-example-screenshot.png "Example Screenshot")

Interested? Here's how it works:

### Getting Started
You will need to be using SBT 0.13.5 or higher for this plugin to work correctly.
It was most extensively tested with SBT 0.13.6.

Add the following line to your plugins.sbt file:

    addSbtPlugin("com.scalapenos" % "sbt-prompt" % "0.1")

For old-style build.sbt projects this will automatically enable the
default prompt theme, which looks like this:

    ...

or like this when your Git working copy is "dirty":

    ...

You can try it by starting an SBT shell in the "default-theme" example project.

If you want to customize the theme or if you are using a full Build.scala
(multi-)project build, you will need to add the following imports:

```scala
import com.scalapenos.sbt.prompt._
import SbtPrompt._
import autoImport._
```

If you want to use one of the existing themes, like the "Scalapenos theme"
show above, just add the following setting to your build.sbt or build.scala:

```scala
promptTheme := PromptThemes.Scalapenos
```


