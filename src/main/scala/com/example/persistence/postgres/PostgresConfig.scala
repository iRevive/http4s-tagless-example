package com.example.persistence.postgres

import com.example.util.execution.Retry
import com.example.util.config.ConfigBoolean
import com.example.util.logging.RenderInstances._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._
import io.circe.config.syntax.durationDecoder
import io.circe.refined._
import io.odin.extras.derivation._
import io.odin.meta.Render

import scala.concurrent.duration.FiniteDuration

@scalaz.deriving(Render)
final case class PostgresConfig(
    driver: NonEmptyString,
    uri: NonEmptyString,
    @secret user: NonEmptyString,
    @secret password: String,
    connectionAttemptTimeout: FiniteDuration,
    runMigration: ConfigBoolean,
    retryPolicy: Retry.Policy
)

object PostgresConfig {

  implicit val configuration: Configuration     = Configuration.default.withKebabCaseMemberNames
  implicit val decoder: Decoder[PostgresConfig] = deriveConfiguredDecoder

}