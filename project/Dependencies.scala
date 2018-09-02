import sbt._


object Dependencies extends AutoPlugin {
  object autoImport {
    val `paradise` = "org.scalamacros" % "paradise" % "2.1.0-M5"

    val `imp`             = "org.spire-math"             %% "imp"                % "0.2.1"

    val `scalaz-core`     = "org.scalaz"                 %% "scalaz-core"        % "7.1.4"
    val `scalaz-contrib`  = "org.typelevel"              %% "scalaz-contrib-210" % "0.2"

    val `refined`         = "eu.timepit"                 %% "refined"            % "0.2.3"

    val `logback-classic` = "ch.qos.logback"              % "logback-classic"    % "1.1.3"
    val `scala-logging`   = "com.typesafe.scala-logging" %% "scala-logging"      % "3.1.0"

    val `akka-actor`           = "com.typesafe.akka"          %% "akka-actor"                        % "2.5.16"
    val `akka-slf4j`           = "com.typesafe.akka"          %% "akka-slf4j"                        % `akka-actor`.revision
    val `akka-testkit`         = "com.typesafe.akka"          %% "akka-testkit"                      % `akka-actor`.revision
    val `akka-http`            = "com.typesafe.akka"          %% "akka-http-experimental"            % "1.0"
    val `akka-http-testkit`    = "com.typesafe.akka"          %% "akka-http-testkit-experimental"    % `akka-http`.revision
    val `akka-http-spray-json` = "com.typesafe.akka"          %% "akka-http-spray-json-experimental" % `akka-http`.revision
    val `spray-json`           = "io.spray"                   %% "spray-json"                        % "1.3.2"
    val `json-lenses`          = "net.virtual-void"           %% "json-lenses"                       % "0.6.1"

    val `parboiled2`           = "org.parboiled" %% "parboiled" % "2.1.0"

    val `scalatest` = "org.scalatest" %% "scalatest" % "2.2.5"
  }
}
