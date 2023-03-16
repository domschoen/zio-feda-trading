package trading.state


import trading.domain.*
import trading.domain.Price
import zio.optics.Lens
//import zio.schema.*
//import zio.schema.Schema.Field
import zio._
import zio.optics._

final case class TradeState(
                          status: TradingStatus,
                          prices: Map[Symbol, Prices]
                        ) {
  val emptyPrice = scala.math.BigDecimal(0.0)

  def updateTradeStateWithFunction(s: Symbol, ts: TradeState, f: Prices => Either[Nothing, Prices]):
    Task[TradeState] = {
      val fixedState: TradeState = TradeState.__Prices.at(s).get(ts) match {
        case Right(prices) => ts
        case Left(_) => TradeState._Prices.update(ts)(m => m + (s -> Prices.empty)).merge
      }
      val eitherResult = TradeState.__Prices.at(s).update(fixedState)(ps => {
        f(ps).merge
      })
      eitherResult match {
        case Left(of) => ZIO.fail(of)
        case Right(r)  => ZIO.succeed(r)
      }
    }


  def opticResultRight(defaultTs: TradeState, r: OpticResult[OpticFailure,TradeState]): Task[TradeState] =
    r match {
      case Right(ts) => ZIO.succeed(ts)
      case Left(e) => ZIO.fail(e)
    }

  def modify(symbol: Symbol)(action: TradeAction, price: Price, quantity: Quantity): Task[TradeState] = {
    // An update of Prices using optics
    val h = (x: Prices) => Prices._High.update(x)(p => if price > p then price else p)
    val l = (x: Prices) => Prices._Low.update(x)(p => if price < p || (p == emptyPrice) then price else p)

    action match
      case TradeAction.Ask =>
        // Update a Prices ask map key value having key = price with quantity
        val f = (x: Prices) => Prices._Ask.update(x)(_.updated(price, quantity))
        val g = (x: Prices) => f(x).flatMap(h).flatMap(l)
        updateTradeStateWithFunction(symbol, this,g)
      case TradeAction.Bid =>
        val f = (x: Prices) => Prices._Bid.update(x)(_.updated(price, quantity))
        val g = (x: Prices) => f(x).flatMap(h).flatMap(l)
        updateTradeStateWithFunction(symbol, this,g)
  }

  def remove(symbol: Symbol)(action: TradeAction, price: Price): Task[TradeState] =
    action match
      case TradeAction.Ask =>
        opticResultRight(this, TradeState
          .__AskPrices(symbol)(this).update(ask => ask.removed(price)))
      case TradeAction.Bid =>
        opticResultRight(this, TradeState
          .__BidPrices(symbol)(this).update(bid => bid.removed(price)))
}


object TradeState {
  def empty: TradeState = TradeState(TradingStatus.On, Map.empty)

  //  import Schema.*
  //
  //  given schema: Schema[TradeState] = DeriveSchema.gen[TradeState]

  val _Status: Lens[TradeState, TradingStatus] =
    Lens(
      tradeState => Right(tradeState.status),
      status => tradeState => Right(tradeState.copy(status = status))
    )


  val _Prices: Lens[TradeState, Map[Symbol, Prices]] =
    Lens(
      tradeState => Right(tradeState.prices),
      prices => tradeState => Right(tradeState.copy(prices = prices))
    )


  object __Prices {
    def at(s: Symbol): Optional[TradeState, Prices] =
      _Prices >>> Optic.key(s)
  }

  object __AskPrices {
    def apply(s: Symbol): Optional[TradeState, Prices.Ask] =
      __Prices.at(s) >>> Prices._Ask
  }

  object __BidPrices {
    def apply(s: Symbol): Optional[TradeState, Prices.Bid] =
      __Prices.at(s) >>> Prices._Bid
  }

}


final case class Prices(
                         ask: Prices.Ask,
                         bid: Prices.Bid,
                         high: Price,
                         low: Price
                       )

object Prices {
  //import Schema.*
  import trading.domain.AskPrice
  import trading.domain.Price
  import scala.math.BigDecimal

  def empty: Prices = Prices(
    scala.collection.immutable.Map.empty[AskPrice, Quantity],
    scala.collection.immutable.Map.empty[AskPrice, Quantity], BigDecimal(0.0), BigDecimal(0.0))


  type Ask = scala.collection.immutable.Map[AskPrice, Quantity]
  type Bid = scala.collection.immutable.Map[BidPrice, Quantity]

  val _Ask: Lens[Prices, Prices.Ask] =
    Lens(
      prices => Right(prices.ask),
      ask => prices => Right(prices.copy(ask = ask))
    )
  val _Bid : Lens[Prices, Prices.Bid] =
    Lens(
      prices => Right(prices.bid),
      bid => prices => Right(prices.copy(bid = bid))
    )
  val _High: Lens[Prices, Price] =
    Lens(
      prices => Right(prices.high),
      high => prices => Right(prices.copy(high = high))
    )
  val _Low: Lens[Prices, Price] =
    Lens(
      prices => Right(prices.low),
      low => prices => Right(prices.copy(low = low))
    )

  //given schema: Schema[Prices] = DeriveSchema.gen[Prices]
}
