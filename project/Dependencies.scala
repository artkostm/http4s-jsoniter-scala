import sbt._

object Dependencies {
  val versions = new {
    val jsoniter = "0.29.2"
    val http4s = "0.18.14"
    val specs2 = "4.1.0"
  }

  lazy val jsoniter = Seq(
    "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % versions.jsoniter % Compile,
    "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % versions.jsoniter % Provided
  )

  lazy val http4s = Seq(
    "org.http4s" %% "http4s-core"
  ).map(_ % versions.http4s)
  
  lazy val spec2 = Seq(
    "org.specs2" %% "specs2-core" % versions.specs2 % Test
  )
  
  lazy val http4sTest = Seq(
    "org.http4s"      %% "http4s-blaze-server",
    "org.http4s"      %% "http4s-dsl",
  ).map(_ % versions.http4s % Test)
}
