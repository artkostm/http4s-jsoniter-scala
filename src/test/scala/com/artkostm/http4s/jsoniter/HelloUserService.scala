package com.artkostm.http4s.jsoniter

import cats.effect.Effect
import cats.implicits._
import org.http4s._
import org.http4s.dsl._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HelloUserService[F[_]: Effect] extends Http4sDsl[F] {
  private val user = User("Test User", 10)

  val service: HttpService[F] = HttpService[F] {
      case req @ POST -> Root / "test1" =>
        for {
          user <- req.as[User]
          resp <- Ok(user)
        } yield resp
      case GET -> Root / "test2" / name => Ok(User(name, 20))
      case GET -> Root / "test3"        => Ok("hello")
      case GET -> Root / "test4"        => Ok(Future.successful(user))
    }
}
