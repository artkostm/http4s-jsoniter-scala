package com.artkostm.http4s.jsoniter

import cats.effect.IO
import com.github.plokhotnyuk.jsoniter_scala.core.writeToArray
import org.http4s._
import org.http4s.implicits._
import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification

class HelloUserSpec extends Specification {
  private[this] val userName = "username"
  private[this] val userJson = writeToArray(User("user name", 10))

  "HelloUser" >> {
    "test1 should convert json body to object and return correct response" >> {
      uriReturnsCorrectJson(test1, new String(userJson))
    }

    "test2 should return correct user json" >> {
      uriReturnsCorrectJson(test2, s"""{"name":"$userName","age":20}""")
    }

    "test3 should return correct word" >> {
      uriReturnsCorrectJson(test3, "hello")
    }
  }

  private[this] val test1: Response[IO] = {
    val postU = Request[IO](Method.POST, Uri.uri("/test1"), body = fs2.Stream.fromIterator[IO, Byte](userJson.toIterator))
    new HelloUserService[IO].service.orNotFound(postU).unsafeRunSync()
  }

  private[this] val test2: Response[IO] = {
    val getHU = Request[IO](Method.GET, Uri.fromString(s"/test2/$userName").getOrElse(Uri.uri("/test2")))
    new HelloUserService[IO].service.orNotFound(getHU).unsafeRunSync()
  }

  private[this] val test3: Response[IO] = {
    val postU = Request[IO](Method.GET, Uri.uri("/test3"))
    new HelloUserService[IO].service.orNotFound(postU).unsafeRunSync()
  }

  private[this] def uriReturnsCorrectJson(response: Response[IO], json: String): MatchResult[String] =
    response.as[String].unsafeRunSync() must beEqualTo(json)
}
