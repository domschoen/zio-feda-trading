package trading.core

import trading.commands.*
import trading.core.TradeEngine.fsm
import trading.domain.TradingStatus.*
import trading.domain.*
import trading.events.*
import trading.state.*
import zio.ZIO
import zio.test.{Assertion, ZIOSpecDefault, assertTrue, assertZIO}

import java.time.Instant
import java.util.UUID

object TradeEngineSpec extends ZIOSpecDefault:
  val id = CommandId(UUID.randomUUID())
  val eid = EventId(UUID.randomUUID())
  val cid = CorrelationId(UUID.randomUUID())
  val s = Symbol.EURUSD
  val ts = Timestamp(Instant.parse("2021-09-16T14:00:00.00Z"))

  val p1 = Price(1.1987)
  val q1 = Quantity(10)

  val p2 = Price(3.5782)
  val q2 = Quantity(20)

  val cmd1 = TradeCommand.Create(id, cid, s, TradeAction.Ask, p1, q1, "test", ts)
  val task1 = fsm.run(TradeState.empty, cmd1) // Task(st1, ev1)
  val xst1 = TradeState(On, Map(s -> Prices(ask = Map(p1 -> q1), bid = Map.empty, p1, p1)))
  val xev1 = TradeEvent.CommandExecuted(eid, cid, cmd1, ts)

  val cmd2 = TradeCommand.Update(id, cid, s, TradeAction.Ask, p2, q2, "test", ts)
  val task2 = task1.flatMap(sta => fsm.run(sta._1, cmd2)) // (st2, ev2)
  val xst2 = TradeState(On, Map(s -> Prices(ask = Map(p1 -> q1, p2 -> q2), bid = Map.empty, p2, p1)))
  val xev2 = TradeEvent.CommandExecuted(eid, cid, cmd2, ts)

  val cmd3 = TradeCommand.Delete(id, cid, s, TradeAction.Ask, p1, "test", ts)
  val task3 = task2.flatMap(t2 => fsm.run(t2._1, cmd3)) // (st3, ev3)
  val xst3 = TradeState(On, Map(s -> Prices(ask = Map(p2 -> q2), bid = Map.empty, p2, p1)))
  val xev3 = TradeEvent.CommandExecuted(eid, cid, cmd3, ts)

  val cmd4 = TradeCommand.Create(id, cid, s, TradeAction.Bid, p1, q1, "test", ts)
  val task4 = task3.flatMap(t3 => fsm.run(t3._1, cmd4)) // (st4, ev4)
  val xst4 = TradeState(On, Map(s -> Prices(ask = Map(p2 -> q2), bid = Map(p1 -> q1), p2, p1)))
  val xev4 = TradeEvent.CommandExecuted(eid, cid, cmd4, ts)

  val cmd5 = SwitchCommand.Stop(id, cid, ts)
  val task5 = task4.flatMap(t4 => fsm.run(t4._1, cmd5)) // (st5, ev5)
  val xst5 = TradeState(Off, xst4.prices)
  val xev5 = SwitchEvent.Stopped(eid, cid, ts)

  val cmd6 = TradeCommand.Create(id, cid, s, TradeAction.Bid, p1, q1, "test", ts)
  val task6 = task5.flatMap(t5 => fsm.run(t5._1, cmd6)) // (st6, ev6)
  val xst6 = xst5
  val xev6 = TradeEvent.CommandRejected(eid, cid, cmd6, Reason("Trading is Off"), ts)


  def spec =
    suite("Trade engine")(
      suite("Trade engine commands fsm") (
        test("Ask command") {
          assertZIO(task1.map(sta => sta._1))(Assertion.equalTo(xst1))
        },
        test("Ask command on state 1") {
          assertZIO(task2.map(sta => sta._1))(Assertion.equalTo(xst2))
        },
        test("Ask command on state 2") {
          assertZIO(task3.map(sta => sta._1))(Assertion.equalTo(xst3))
        },
        test("Ask command on state 3") {
          assertZIO(task4.map(sta => sta._1))(Assertion.equalTo(xst4))
        },
        test("Ask command on state 4") {
          assertZIO(task5.map(sta => sta._1))(Assertion.equalTo(xst5))
        },
        test ("Ask command on state 5") {
          assertZIO(task6.map(sta => sta._1))(Assertion.equalTo(xst6))
        },
        test("Output 1") {
          assertZIO(task1.map(sta => sta._2(eid, ts)))(Assertion.equalTo(xev1))
        },
        test("Output 2") {
          assertZIO(task2.map(sta => sta._2(eid, ts)))(Assertion.equalTo(xev2))
        },
        test("Output 3") {
          assertZIO(task3.map(sta => sta._2(eid, ts)))(Assertion.equalTo(xev3))
        },
        test("Output 4") {
          assertZIO(task4.map(sta => sta._2(eid, ts)))(Assertion.equalTo(xev4))
        },
        test("Output 5") {
          assertZIO(task5.map(sta => sta._2(eid, ts)))(Assertion.equalTo(xev5))
        },
        test("Output 6") {
          assertZIO(task6.map(sta => sta._2(eid, ts)))(Assertion.equalTo(xev6))
        },
        test("Output 7") {
          assertZIO(task1.map(sta => sta._2(eid, ts)))(Assertion.equalTo(xev1))
        }

      )
    )


