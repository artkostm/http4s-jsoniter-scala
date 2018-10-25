package com.artkostm.http4s.jsoniter

import java.nio.ByteBuffer

import cats.Applicative
import cats.effect.Sync
import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, readFromArray, writeToArray}
import org.http4s.{DecodeResult, EntityDecoder, EntityEncoder, MalformedMessageBodyFailure, MediaType}
import org.http4s.headers.`Content-Type`

import scala.util.{Failure, Success, Try}

trait JsoniterInstances {
  implicit def jsoniterEntityEncoder[F[_]: Applicative, A: JsonValueCodec]: EntityEncoder[F, A] =
    EntityEncoder.byteArrayEncoder[F]
      .contramap[A](writeToArray(_))
      .withContentType(`Content-Type`(MediaType.`application/json`))

  implicit def jsoniterEntityDecoder[F[_]: Sync, A: JsonValueCodec]: EntityDecoder[F, A] =
    EntityDecoder.decodeBy(MediaType.`application/json`) { msg =>
      EntityDecoder.collectBinary(msg).flatMap { segment =>
        val bb = ByteBuffer.wrap(segment.force.toArray)
        if (bb.hasRemaining) {
          Try(readFromArray(bb.array())) match {
            case Success(json) =>
              DecodeResult.success[F, A](json)
            case Failure(pf) =>
              DecodeResult.failure[F, A](
                MalformedMessageBodyFailure("Invalid JSON", Some(pf)))
          }
        } else {
          DecodeResult.failure[F, A](MalformedMessageBodyFailure("Invalid JSON: empty body", None))
        }
      }
    }
}
