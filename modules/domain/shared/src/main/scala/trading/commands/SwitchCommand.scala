package trading.commands

import zio.optics.*
import zio.json.*
import zio.schema.*
import zio.schema.Schema.Field

import java.time.Instant
import java.util.UUID
import trading.domain.{ * }

sealed trait SwitchCommand {
  def id: UUID
  def cid: UUID
  def createdAt: Instant
}

object SwitchCommand {
  final case class Start(
                          id: UUID,
                          cid: UUID,
                          createdAt: Instant
                        ) extends SwitchCommand


  object Start {
    import Schema.*
    given schema: Schema[Start] = DeriveSchema.gen[Start]
  }

  final case class Stop(
                          id: UUID,
                          cid: UUID,
                          createdAt: Instant
                        ) extends SwitchCommand


  object Stop {

    import Schema.*

    given schema: Schema[Stop] = DeriveSchema.gen[Stop]
  }

}