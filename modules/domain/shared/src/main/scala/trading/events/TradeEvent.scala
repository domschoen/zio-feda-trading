package trading.events


import trading.commands.TradeCommand
import trading.domain.{ * }

import zio.optics.*
import zio.json.*
import zio.schema.*
import zio.schema.Schema.Field

import java.time.Instant
import java.util.UUID

sealed trait TradeEvent {
  def id: UUID
  def cid: UUID
  def command: TradeCommand
  def createdAt: Instant
}

object TradeEvent {
  final case class CommandExecuted(
                                    id: UUID,
                                    cid: UUID,
                                    command: TradeCommand,
                                    createdAt: Instant
                                  ) extends TradeEvent


  object CommandExecuted {

    import Schema.*

    given schema: Schema[CommandExecuted] = DeriveSchema.gen[CommandExecuted]
  }

  final case class CommandRejected(
                                    id: UUID,
                                    cid: UUID,
                                    command: TradeCommand,
                                    reason: String,
                                    createdAt: Instant
                                  ) extends TradeEvent


  object CommandRejected {

    import Schema.*

    given schema: Schema[CommandRejected] = DeriveSchema.gen[CommandRejected]
  }
}