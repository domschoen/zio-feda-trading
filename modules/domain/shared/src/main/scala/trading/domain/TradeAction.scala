package trading.domain

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

enum TradeAction:
  case Ask, Bid

object TradeActionSerialization {

  import trading.domain.{*, given}

  given encoder: JsonEncoder[TradeAction] = DeriveJsonEncoder.gen[TradeAction]
  given decoder: JsonDecoder[TradeAction] = DeriveJsonDecoder.gen[TradeAction]
}