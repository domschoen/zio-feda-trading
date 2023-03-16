package trading.events

import zio.optics.*
import zio.json.*
import zio.schema.*
import zio.schema.Schema.Field

import java.time.Instant
import java.util.UUID
import trading.domain.{ * }

sealed trait SwitchEvent {
  def id: UUID
  def cid: UUID
  def createdAt: Instant
}

object SwitchEvent {
  final case class Started(
                            id: UUID,
                            cid: UUID,
                            createdAt: Instant
                          ) extends SwitchEvent


  object Started {
    import Schema.*
    given schema: Schema[Started] = DeriveSchema.gen[Started]
  }

  final case class Stopped(
                            id: UUID,
                            cid: UUID,
                            createdAt: Instant
                          ) extends SwitchEvent


  object Stopped {
    import Schema.*
    given schema: Schema[Stopped] = DeriveSchema.gen[Stopped]
  }

  final case class Ignored(
                            id: UUID,
                            cid: UUID,
                            createdAt: Instant
                          ) extends SwitchEvent


  object Ignored {
    import Schema.*
    given schema: Schema[Ignored] = DeriveSchema.gen[Ignored]
  }

}