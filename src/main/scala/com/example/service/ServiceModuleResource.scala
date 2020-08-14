package com.example.service

import cats.effect._
import com.example.persistence.PersistenceModule
import com.example.service.user.UserService
import com.example.service.user.api.UserApi
import com.example.service.user.domain.UserRepository
import com.example.util.error.{ErrorHandle, ErrorIdGen}
import com.example.util.logging.TraceProvider
import com.typesafe.config.Config
import io.odin.Logger

class ServiceModuleResource[F[_]: Sync: ErrorHandle: TraceProvider: ErrorIdGen: Logger] {

  def create(rootConfig: Config, persistenceModule: PersistenceModule[F]): Resource[F, ServiceModule[F]] = {
    val _       = rootConfig
    val userApi = new UserApi[F](new UserService[F](new UserRepository[F](persistenceModule.transactor)))
    Resource.pure(ServiceModule[F](userApi))
  }

}
