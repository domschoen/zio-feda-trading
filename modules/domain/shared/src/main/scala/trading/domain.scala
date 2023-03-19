package trading.domain

import zio.prelude.Newtype
import java.time.Instant
import java.util.UUID
import scala.math.BigDecimal

//object Reason extends Newtype[String]
//type Reason = Reason.Type

type Price = BigDecimal
object Price {
  def apply(c: Price): Price = c
}

type AskPrice = Price
type BidPrice = Price

type Timestamp = Instant
object Timestamp {
  def apply(c: Timestamp): Timestamp = c
}

type Quantity = Int
object Quantity {
  def apply(c: Quantity): Quantity = c
}

type Source = String
object Source {
  def apply(c: Source): Source = c
}

type CommandId = UUID
object CommandId {
  def apply(c: CommandId): CommandId = c
}

type CorrelationId = UUID
object CorrelationId {
  def apply(c: CorrelationId): CorrelationId = c
}

type EventId = UUID
object EventId {
  def apply(c: EventId): EventId = c
}

type Reason = String
object Reason {
  def apply(c: Reason): Reason = c
}