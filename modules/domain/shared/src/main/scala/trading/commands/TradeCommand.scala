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
    import Schema.*
    import trading.domain.{ * , given }

    given schema: Schema[Create] = DeriveSchema.gen[Create]
  }

  final case class Update(
                           id: UUID,
                           cid: UUID,
                           symbol: Symbol,
                           tradeAction: TradeAction,
                           price: BigDecimal,
                           quantity: Int,
                           source: String,
                           createdAt: Instant
                        ) extends TradeCommand

  object Update {
    import Schema.*
    given schema: Schema[Update] = DeriveSchema.gen[Update]
  }

  final case class Delete(
                           id: UUID,
                           cid: UUID,
                           symbol: Symbol,
                           tradeAction: TradeAction,
                           price: BigDecimal,
                           source: String,
                           createdAt: Instant
                         ) extends TradeCommand

  object Delete {
    import Schema.*
    given schema: Schema[Delete] = DeriveSchema.gen[Delete]
  }

}
/*
  case Update(
               id: CommandId,
               cid: CorrelationId,
               symbol: Symbol,
               tradeAction: TradeAction,
               price: Price,
               quantity: Quantity,
               source: Source,
               createdAt: Timestamp
             )

  case Delete(
               id: CommandId,
               cid: CorrelationId,
               symbol: Symbol,
               tradeAction: TradeAction,
               price: Price,
               source: Source,
               createdAt: Timestamp
             )*/
/*
object TradeCommand:
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