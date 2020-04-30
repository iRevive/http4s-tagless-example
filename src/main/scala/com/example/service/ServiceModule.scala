package com.example.service

import com.example.service.user.api.UserApi

final case class ServiceModule[F[_]](userApi: UserApi[F])
