package trading.commands

import trading.domain.{ * , given }

import zio.optics.*
import zio.json.*
import zio.schema.*
import zio.schema.Schema.Field
import java.time.Instant
import java.util.UUID

sealed trait TradeCommand {
  def id: UUID
  def cid: UUID
  def symbol: Symbol
  def createdAt: Instant
}

object TradeCommand {
  import trading.domain.{ *, given }
  import trading.domain.TradeActionSerialization.{ *, given }


  final case class Create(
                           id: UUID,
                           cid: UUID,
                           symbol: Symbol,
                           tradeAction: TradeAction,
                           price: BigDecimal,
                           quantity: Int,
                           source: String,
                           createdAt: Instant
                         ) extends TradeCommand


  object Create {
    //import Schema.*
    //given schema: Schema[Create] = DeriveSchema.gen[Create]

    import trading.domain.{ * , given }

    import trading.domain.TradeActionSerialization.encoder
    import trading.domain.TradeActionSerialization.decoder

    given encoder: JsonEncoder[Create] = DeriveJsonEncoder.gen[Create]
    given decoder: JsonDecoder[Create] = DeriveJsonDecoder.gen[Create]
  }

  final case class Update(
                           id: CommandId,
                           cid: CorrelationId,
                           symbol: Symbol,
                           tradeAction: TradeAction,
                           price: Price,
                           quantity: Quantity,
                           source: Source,
                           createdAt: Timestamp
                        ) extends TradeCommand

  object Update {
//    import Schema.*
//    given schema: Schema[Update] = DeriveSchema.gen[Update]

    import trading.domain.{*, given}
    import trading.domain.TradeActionSerialization.encoder
    import trading.domain.TradeActionSerialization.decoder

    given encoder: JsonEncoder[Update] = DeriveJsonEncoder.gen[Update]
    given decoder: JsonDecoder[Update] = DeriveJsonDecoder.gen[Update]
  }

  final case class Delete(
                           id: CommandId,
                           cid: CorrelationId,
                           symbol: Symbol,
                           tradeAction: TradeAction,
                           price: Price,
                           source: Source,
                           createdAt: Timestamp
                         ) extends TradeCommand

  object Delete {
//  import Schema.*
//  given schema: Schema[Delete] = DeriveSchema.gen[Delete]
    import trading.domain.{*, given}

    import trading.domain.TradeActionSerialization.encoder
    import trading.domain.TradeActionSerialization.decoder

    given encoder: JsonEncoder[Update] = DeriveJsonEncoder.gen[Update]
    given decoder: JsonDecoder[Update] = DeriveJsonDecoder.gen[Update]
  }

  implicit lazy val encoder: JsonEncoder[TradeCommand] = DeriveJsonEncoder.gen[TradeCommand]
  implicit lazy val decoder: JsonDecoder[TradeCommand] = DeriveJsonDecoder.gen[TradeCommand]

  /*
    val _CommandId: Traversal[TradeCommand, CommandId] = new:
      def modifyA[F[_]: Applicative](f: CommandId => F[CommandId])(s: TradeCommand): F[TradeCommand] =
        f(s.id).map { newId =>
          s match
            case c: Create => c.copy(id = newId)
            case c: Update => c.copy(id = newId)
            case c: Delete => c.copy(id = newId)
        }

    val _CorrelationId: Traversal[TradeCommand, CorrelationId] = new:
      def modifyA[F[_]: Applicative](f: CorrelationId => F[CorrelationId])(s: TradeCommand): F[TradeCommand] =
        f(s.cid).map { newCid =>
          s match
            case c: Create => c.copy(cid = newCid)
            case c: Update => c.copy(cid = newCid)
            case c: Delete => c.copy(cid = newCid)
        }

    val _CreatedAt: Traversal[TradeCommand, Timestamp] = new:
      def modifyA[F[_]: Applicative](f: Timestamp => F[Timestamp])(s: TradeCommand): F[TradeCommand] =
        f(s.createdAt).map { ts =>
          s match
            case c: Create => c.copy(createdAt = ts)
            case c: Update => c.copy(createdAt = ts)
            case c: Delete => c.copy(createdAt = ts)
        }
  */
}
