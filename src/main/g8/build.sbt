lazy val commonSettings = Seq(
  organization := "$organization$",
  version := "$version$",
  scalaVersion := "2.11.8",
  resolvers += Resolver.sonatypeRepo("releases"),
  updateOptions := updateOptions.value.withCachedResolution(true),
  addCompilerPlugin(
    "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "org.scalacheck" %%% "scalacheck" % "$scalacheck_version$" % "test"
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked",
    "-target:jvm-1.8",
    "-encoding", "UTF-8",
    "-Xfuture",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-unused",
    "-feature",
    "-Xlint"
  )
)

lazy val server = project
  .enablePlugins(PlayScala)
  .dependsOn(sharedJvm)
  .settings(commonSettings: _*)
  .settings(
    // dev asset pipeline
    pipelineStages in Assets := Seq(scalaJSPipeline),
    // prod asset pipeline
    pipelineStages := Seq(concat, digest, gzip),
    scalaJSProjects := Seq(client),
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,

    libraryDependencies ++= Seq(
      cache,
      "com.vmunier" %% "scalajs-scripts" % "$scalajs_scripts_version$"
    )
  )

lazy val client = project
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(sharedJs)
  .settings(commonSettings: _*)
  .settings(
    persistLauncher := true
  )

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .jsConfigure(_ enablePlugins ScalaJSWeb)
  .settings(commonSettings: _*)

lazy val sharedJs = shared.js
lazy val sharedJvm = shared.jvm

// loads the server project at sbt startup
onLoad in Global := (Command
  .process("project server", _: State)) compose (onLoad in Global).value
