lazy val root = (project in file("."))
  .settings(
    organization := "Samtakoj",
    name := "http4s-jsoniter-scala",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.7",
    libraryDependencies ++= Dependencies.http4s,
    libraryDependencies ++= Dependencies.jsoniter,
    libraryDependencies ++= Dependencies.spec2,
    libraryDependencies ++= Dependencies.http4sTest,
    scalacOptions       := Seq(
      "-feature",
      "-encoding",
      "-deprecation",
      "UTF-8",
      "-language:higherKinds",
      "-language:existentials",
      "-language:implicitConversions",
      "-Ypartial-unification"
    )
  )
