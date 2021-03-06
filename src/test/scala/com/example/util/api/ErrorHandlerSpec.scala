package com.example.util.api

import cats.mtl.implicits._
import cats.mtl.Handle.handleKleisli
import com.example.persistence.postgres.PostgresError
import com.example.test.BaseSpec
import com.example.util.error.{AppError, RaisedError}
import com.example.util.logging.Loggers
import io.circe.syntax._
import io.odin.Level
import io.odin.meta.Position
import org.http4s._
import org.http4s.circe._
import org.http4s.headers.`Content-Type`
import org.http4s.syntax.kleisli._
import org.http4s.syntax.literals._
import shapeless.syntax.inject._

class ErrorHandlerSpec extends BaseSpec {

  import com.example.service.user.api.UserValidationErrorResponse._

  "ErrorHandler" should {

    "return result" in EffectAssertion() {
      val defaultResponse = Response[Eff]().withEntity(ApiResponse.Success("value").asJson)
      val routes          = mkRoutes(Eff.pure(defaultResponse))
      val middleware      = ErrorHandler.httpRoutes[Eff](Loggers.consoleContextLogger(Level.Info))(routes).orNotFound

      for {
        response <- middleware.run(Request[Eff](Method.GET, uri"/api/endpoint"))
        body     <- response.as[String]
      } yield {
        val expectedBody = """{"success":true,"result":"value"}"""

        response.status shouldBe Status.Ok
        response.contentType shouldBe Some(`Content-Type`(MediaType.application.json))
        body shouldBe expectedBody
      }
    }

    "handle checked error" in EffectAssertion() {
      val error = RaisedError(
        PostgresError.connectionAttemptTimeout("error").inject[AppError],
        Position.derivePosition,
        "test"
      )

      val routes     = mkRoutes(error.raise[Eff, Response[Eff]])
      val middleware = ErrorHandler.httpRoutes[Eff](Loggers.consoleContextLogger(Level.Info))(routes).orNotFound

      for {
        response <- middleware.run(Request[Eff](Method.GET, uri"/api/endpoint"))
        body     <- response.as[String]
      } yield {
        val expectedBody = """{"success":false,"error":"Postgres connection timeout","errorId":"test"}"""

        response.status shouldBe Status.BadRequest
        response.contentType shouldBe Some(`Content-Type`(MediaType.application.json))
        body shouldBe expectedBody
      }
    }

    "handled exception" in EffectAssertion() {
      val error      = new RuntimeException("Something went wrong"): Throwable
      val routes     = mkRoutes(Eff.raiseError(error))
      val middleware = ErrorHandler.httpRoutes[Eff](Loggers.consoleContextLogger(Level.Info))(routes).orNotFound

      for {
        response <- middleware.run(Request[Eff](Method.GET, uri"/api/endpoint"))
        body     <- response.as[String]
      } yield {
        val expectedBody = """{"success":false,"error":"Unhandled internal error","errorId":"test"}"""

        response.status shouldBe Status.InternalServerError
        response.contentType shouldBe Some(`Content-Type`(MediaType.application.json))
        body shouldBe expectedBody
      }
    }

  }

  private def mkRoutes(response: Eff[Response[Eff]]): HttpRoutes[Eff] = {
    import org.http4s.dsl.impl.{->, /, Root}

    HttpRoutes.of[Eff] { case Method.GET -> Root / "api" / "endpoint" =>
      response
    }
  }

}
