package com.example.persistence.postgres

import java.time.Instant

import cats.Eq
import com.example.util.instances.render._
import io.odin.meta.Render

@scalaz.annotation.deriving(Eq, Render)
final case class Persisted[PK, A](
    id: PK,
    createdAt: Instant,
    updatedAt: Instant,
    deletedAt: Option[Instant],
    entity: A
)
