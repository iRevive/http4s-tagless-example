package com.example.service.user

import com.example.service.user.domain.UserId
import com.example.util.error.ThrowableSelect
import io.odin.meta.Render

@scalaz.annotation.deriving(Render, ThrowableSelect.Empty)
sealed trait UserValidationError

object UserValidationError {

  final case class UserNotFound(userId: UserId) extends UserValidationError

  def userNotFound(userId: UserId): UserValidationError = UserNotFound(userId)

}
