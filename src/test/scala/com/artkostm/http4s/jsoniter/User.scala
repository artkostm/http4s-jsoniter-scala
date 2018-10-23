package com.artkostm.http4s.jsoniter

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.{CodecMakerConfig, JsonCodecMaker}

final case class User(name: String, age: Int)

object User {
  implicit val codec: JsonValueCodec[User] = JsonCodecMaker.make[User](CodecMakerConfig())
}
