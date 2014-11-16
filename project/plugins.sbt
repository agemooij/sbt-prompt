addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")

resolvers += Resolver.url("bintray-sbt-plugin-releases", url("https://dl.bintray.com/content/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.1.2")

addSbtPlugin("me.lessis" % "ls-sbt" % "0.1.3")
