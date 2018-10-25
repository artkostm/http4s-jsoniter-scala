package com.artkostm.http4s.jsoniter

import java.util.{Random, UUID}

import cats.effect.IO
import com.github.plokhotnyuk.jsoniter_scala.core.writeToArray
import org.http4s._
import org.http4s.implicits._
import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification

class HelloUserSpec extends Specification {
  import HelloUserSpec._

  "HelloUser" >> {
    "test1 should convert json body to object and return correct response" >> {
      uriReturnsCorrectJson(test1, new String(userJson))
    }

    "test2 should return correct user json" >> {
      uriReturnsCorrectJson(test2, new String(writeToArray(user.copy(userName))))
    }

    "test3 should return correct string" >> {
      uriReturnsCorrectJson(test3, greeting)
    }

    "test4 should return correct user json from future" >> {
      uriReturnsCorrectJson(test4, new String(userJson))
    }

    "test5 should return first user" >> {
      uriReturnsCorrectJson(test5, new String(writeToArray(users(0))))
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

  private[this] val test4: Response[IO] = {
    val getHU = Request[IO](Method.GET, Uri.uri("/test4"))
    new HelloUserService[IO].service.orNotFound(getHU).unsafeRunSync()
  }

  private[this] val test5: Response[IO] = {
    val postU = Request[IO](Method.POST, Uri.uri("/test5"), body = fs2.Stream.fromIterator[IO, Byte](writeToArray(users).toIterator))
    new HelloUserService[IO].service.orNotFound(postU).unsafeRunSync()
  }

  private[this] def uriReturnsCorrectJson(response: Response[IO], json: String): MatchResult[String] =
    response.as[String].unsafeRunSync() must beEqualTo(json)
}

object HelloUserSpec {
  protected[jsoniter] val user = User("user name", 10)
  protected[jsoniter] val greeting = "hello"
  protected val userName = "username"
  protected val userJson = writeToArray(User("user name", 10))
  protected lazy val users = (1 to 5).map(id => User(UUID.randomUUID().toString, id * new Random().nextInt(10))).toList
}
