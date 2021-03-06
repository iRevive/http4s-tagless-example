package com.example.util.execution

import cats.Applicative
import com.example.util.error.ThrowableSelect
import com.example.util.instances.render._
import eu.timepit.refined.auto._
import eu.timepit.refined.types.numeric.NonNegInt
import io.circe.Decoder
import io.circe.config.syntax.durationDecoder
import io.circe.refined._
import io.odin.Logger
import io.odin.meta.Render
import io.odin.syntax._
import retry.{RetryDetails, RetryPolicies, RetryPolicy}

import scala.concurrent.duration.FiniteDuration

object Retry {

  def makePolicy[F[_]: Applicative](retryPolicy: Policy): RetryPolicy[F] = {
    val policy = RetryPolicies
      .constantDelay(retryPolicy.delay)
      .join(RetryPolicies.limitRetries(retryPolicy.retries))

    RetryPolicies.limitRetriesByCumulativeDelay(retryPolicy.timeout, policy)
  }

  def logErrors[F[_]: Applicative, E: Render: ThrowableSelect](logger: Logger[F]): (E, RetryDetails) => F[Unit] =
    (error, details) => {
      ThrowableSelect[E].select(error) match {
        case Some(cause) => logger.error(render"Retry policy. Error $error. $details", cause)
        case None        => logger.error(render"Retry policy. Error $error. $details")
      }
    }

  @scalaz.annotation.deriving(Decoder, Render)
  final case class Policy(retries: NonNegInt, delay: FiniteDuration, timeout: FiniteDuration)

  implicit val renderRetryDetails: Render[RetryDetails] = io.odin.extras.derivation.render.derive

}
