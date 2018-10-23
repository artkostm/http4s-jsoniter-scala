package com.artkostm.http4s.jsoniter

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification

class HelloUserSpec extends Specification {
  private[this] val userName = "username"

  "HelloUser" >> {
    "test2 returns correct user json" >> {
      uriReturnsCorrectJson(test2, s"""{"name":"$userName","age":20}""")
    }
  }

  private[this] val test2: Response[IO] = {
    val getHW = Request[IO](Method.GET, Uri.fromString(s"/test2/$userName").getOrElse(Uri.uri("/test2")))
    new HelloUserService[IO].service.orNotFound(getHW).unsafeRunSync()
  }

  private[this] def uriReturnsCorrectJson(response: Response[IO], json: String): MatchResult[String] =
    response.as[String].unsafeRunSync() must beEqualTo(json)
}
