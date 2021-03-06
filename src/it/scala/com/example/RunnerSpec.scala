package com.example

import cats.data.{EitherT, Kleisli}
import cats.effect.ExitCode
import cats.syntax.either._
import com.example.it.ITSpec
import com.example.persistence.postgres.PostgresError
import com.example.util.error.{AppError, RaisedError}
import io.odin.meta.Position
import shapeless.syntax.inject._

class RunnerSpec extends ITSpec {

  "Runner" should {

    "execute a job via main method" in {
      val runner = new Runner.Default {
        override def name: String = "test"

        override def job: Kleisli[Eff, ApplicationResource.Application[Eff], ExitCode] =
          Kleisli.pure(ExitCode.Success)
      }

      noException shouldBe thrownBy(runner.main(Array()))
    }

    "rethrow an exception" in {
      val exception = new RuntimeException("Unhandled error")

      val runner = new Runner.Default {
        override def name: String = "test"

        override def job: Kleisli[Eff, ApplicationResource.Application[Eff], ExitCode] =
          Kleisli.liftF(Eff.raiseError(exception))
      }

      runner.run(Nil).attempt.runSyncUnsafe() shouldBe Left(exception)
    }

    "return checked error" in {
      val error = RaisedError(
        PostgresError.connectionAttemptTimeout("error").inject[AppError],
        Position.derivePosition,
        "errorId"
      )

      val runner = new Runner.Default {
        override def name: String = "test"

        override def job: Kleisli[Eff, ApplicationResource.Application[Eff], ExitCode] =
          Kleisli.liftF(Kleisli.liftF(EitherT.leftT(error)))
      }

      runner.run(Nil).attempt.runSyncUnsafe().leftMap(_.getMessage) shouldBe Left(error.toException.getMessage)
    }

    "respect cancellation" in {
      val runner = new Runner.Default {
        override def name: String = "test"

        override def job: Kleisli[Eff, ApplicationResource.Application[Eff], ExitCode] =
          Kleisli.liftF(Eff.never[ExitCode])
      }

      runner.run(Nil).start.flatMap(f => f.cancel).attempt.runSyncUnsafe() shouldBe Right(())
    }

  }

}
