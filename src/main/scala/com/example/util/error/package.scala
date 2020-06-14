package com.example.util

import cats.mtl.{ApplicativeHandle, FunctorRaise}
import com.example.persistence.postgres.PostgresError
import com.example.service.user.UserValidationError
import com.example.util.config.ConfigParsingError
import com.example.util.json.JsonDecodingError
import shapeless._

package object error {

  type AppError = UserValidationError :+: PostgresError :+: JsonDecodingError :+: ConfigParsingError :+: CNil

  type ErrorRaise[F[_]] = FunctorRaise[F, RaisedError]

  object ErrorRaise {
    def apply[F[_]](implicit instance: ErrorRaise[F]): ErrorRaise[F] = instance
  }

  type ErrorHandle[F[_]] = ApplicativeHandle[F, RaisedError]

  object ErrorHandle {
    def apply[F[_]](implicit instance: ErrorHandle[F]): ErrorHandle[F] = instance
  }

}
