package com.example.util

import cats.data.{EitherT, Kleisli}
import com.example.util.error.RaisedError
import com.example.util.logging.TraceId
import monix.eval.Task

package object execution {

  type Traced[F[_], A] = Kleisli[F, TraceId, A]
  type Eff[A]          = Traced[EitherT[Task, RaisedError, *], A]

}
