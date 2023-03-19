package trading.feed


import java.time.Instant
import java.util.UUID

import zio.kafka.serde._
import zio._
import zio.json._

object Feed {

  import trading.commands.{*, given}
  import trading.commands.TradeCommand.{*, given}
  import trading.domain.{*, given}

  val id = CommandId(UUID.randomUUID())
  val cid = CorrelationId(UUID.randomUUID())
  val s = Symbol.EURUSD
  val ts = Timestamp(Instant.parse("2021-09-16T14:00:00.00Z"))

  val p1 = Price(1.1987)
  val q1 = Quantity(10)

  val cmd = trading.commands.TradeCommand.Create(id, cid, s, TradeAction.Bid, p1, q1, "test", ts)


  object KafkaSerde {
    val key: Serde[Any, Int] =
      Serde.int

    val value: Serde[Any, TradeCommand] =
      Serde.string.inmapM[Any, TradeCommand](s =>
        ZIO.fromEither(s.fromJson[TradeCommand])
          .mapError(e => new RuntimeException(e))
      )(r => ZIO.succeed(r.toJson))
  }

}
