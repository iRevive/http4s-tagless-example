package com.example.util
package json

import cats.data.NonEmptyList
import cats.effect.Sync
import cats.syntax.either._
import cats.syntax.flatMap._
import com.example.util.error.{ErrorIdGen, ErrorRaise, ThrowableSelect}
import com.example.util.syntax.mtl.raise._
import io.circe._
import io.odin.meta.Render

import scala.reflect.ClassTag

trait ToJsonOps {
  final implicit def toJsonOps(json: Json): JsonOps = new JsonOps(json)
}

final class JsonOps(private val json: Json) extends AnyVal {

  def decodeF[F[_]: Sync: ErrorRaise: ErrorIdGen, A: ClassTag: Decoder]: F[A] =
    Sync[F].delay(decode[A]).flatMap(_.pureOrRaise)

  def decode[A](implicit decoder: Decoder[A], ct: ClassTag[A]): Either[JsonDecodingError, A] =
    decoder
      .decodeAccumulating(json.hcursor)
      .toEither
      .leftMap(errors => JsonDecodingError(json, ct.runtimeClass.getSimpleName, errors))

}

@scalaz.annotation.deriving(Render, ThrowableSelect.Empty)
final case class JsonDecodingError(
    json: Json,
    targetClass: String,
    errors: NonEmptyList[DecodingFailure]
)
