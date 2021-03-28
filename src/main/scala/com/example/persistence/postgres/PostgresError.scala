package com.example.persistence.postgres

import com.example.util.error.ThrowableSelect
import com.example.util.instances.render._
import io.odin.meta.Render

@scalaz.annotation.deriving(Render)
sealed trait PostgresError

object PostgresError {

  final case class UnavailableConnection(cause: Throwable) extends PostgresError

  final case class ConnectionAttemptTimeout(message: String) extends PostgresError

  def unavailableConnection(cause: Throwable): PostgresError   = UnavailableConnection(cause)
  def connectionAttemptTimeout(message: String): PostgresError = ConnectionAttemptTimeout(message)

  implicit val postgresErrorThrowableSelect: ThrowableSelect[PostgresError] = {
    case _: ConnectionAttemptTimeout  => None
    case UnavailableConnection(cause) => Option(cause)
  }

}
