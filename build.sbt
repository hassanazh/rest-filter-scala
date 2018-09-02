import Dependencies.autoImport.{`parboiled2`, `scalatest`}

name := "rest-filter-scala"

version := "0.1"

scalaVersion := "2.12.6"

autoCompilerPlugins := true
scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-target:jvm-1.8",
  "-unchecked",
  // advanced
  "-Xcheckinit",
  "-Xfuture",
  "-Xlint:_",
  "-Xlog-free-terms",
  "-Xlog-free-types",
  //  "-Xlog-implicits",
  "-Xlog-reflective-calls",
  "-Xmax-classfile-name", "130",  // avoid problems on eCryptFS
  "-Xmigration:2.11.0",
  "-Xverify",
  // private
  //  "-Ybackend:GenBCode",
  "-Ybreak-cycles",
  "-Yclosure-elim",
  "-Yconst-opt",
  "-Ydead-code",
  // "-Ydelambdafy:method",
  "-Yinline",
  "-Yinline-handlers",
  "-Yinline-warnings",
  "-Yno-adapted-args",
  //  "-Yopt:l:classpath",
  "-Yopt-warnings:at-inline-failed",
  "-Yrangepos",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-unused-import",
  //  "-Ywarn-value-discard",
  "")

unidocSettings
autoAPIMappings := true
apiMappings += file(s"${System.getenv("JAVA_HOME")}/jre/lib/rt.jar") -> url("http://docs.oracle.com/javase/8/docs/api")

libraryDependencies ++= Seq(
  `parboiled2`,
  // Test dependencies
  `scalatest` % "test")