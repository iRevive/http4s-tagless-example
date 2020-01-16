package com.example.service.user.domain

import com.example.util.logging.RenderInstances._
import eu.timepit.refined.types.numeric.PosInt
import doobie.refined.implicits._
import doobie.util.{Get, Put}
import io.estatico.newtype.macros.newtype
import io.odin.meta.Render

// $COVERAGE-OFF$
@SuppressWarnings(Array("org.wartremover.warts.ExplicitImplicitTypes"))
object NewTypes {

  @newtype
  @scalaz.deriving(Render)
  final case class UserId(toInt: PosInt)

  object UserId {
    implicit val userIdGet: Get[UserId] = Get[PosInt].map(UserId.apply)
    implicit val userIdPut: Put[UserId] = Put[PosInt].contramap(_.toInt)
  }

}
// $COVERAGE-ON$
