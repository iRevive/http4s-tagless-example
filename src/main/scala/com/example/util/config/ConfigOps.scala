package com.example.util
package config

import cats.effect.Sync
import cats.syntax.either._
import cats.syntax.flatMap._
import com.example.util.error.{ErrorIdGen, ErrorRaise, ThrowableSelect}
import com.example.util.syntax.mtl.raise._
import com.typesafe.config.{Config, ConfigFactory}
import io.circe.{Decoder, Error, ParsingFailure}
import io.odin.meta.Render

import scala.reflect.ClassTag

trait ToConfigOps {
  final implicit def toConfigOps(config: Config): ConfigOps = new ConfigOps(config)
}

final class ConfigOps(private val config: Config) extends AnyVal {
  import io.circe.config.syntax._

  def loadF[F[_]: Sync: ErrorRaise: ErrorIdGen, A: Decoder: ClassTag](path: String): F[A] =
    Sync[F].delay(load[A](path)).flatMap(_.pureOrRaise)

  def loadMetaF[F[_]: Sync: ErrorRaise: ErrorIdGen, A: Decoder: ClassTag](path: String): F[A] =
    Sync[F].delay(loadMeta[A](path)).flatMap(_.pureOrRaise)

  def load[A: Decoder](path: String)(implicit ct: ClassTag[A]): Either[ConfigParsingError, A] =
    config.as[A](path).leftMap(error => ConfigParsingError(path, ct.runtimeClass.getSimpleName, error))

  def loadMeta[A: Decoder](path: String)(implicit ct: ClassTag[A]): Either[ConfigParsingError, A] =
    parseStringAsConfig(config.getString(path))
      .flatMap(_.as[A])
      .leftMap(error => ConfigParsingError(path, ct.runtimeClass.getSimpleName, error))

  private def parseStringAsConfig(input: => String): Either[Error, Config] =
    Either
      .catchNonFatal(ConfigFactory.parseString(input))
      .leftMap(e => ParsingFailure(s"Couldn't parse [$input] as config", e))

}

@scalaz.annotation.deriving(Render, ThrowableSelect.Empty)
final case class ConfigParsingError(path: String, expectedClass: String, error: Error)
