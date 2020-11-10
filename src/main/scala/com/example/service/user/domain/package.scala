package com.example.service.user

import com.example.persistence.postgres.Persisted

package object domain {

  type UserId = NewTypes.UserId
  val UserId = NewTypes.UserId

  type PersistedUser = Persisted[UserId, User]

}
