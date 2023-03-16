package trading.lib

import zio._

case class FSM[S, I, O](run: (S, I) => Task[(S, O)]):
  def runS: (S, I) => Task[S] =
    (s, i) => run(s, i).map(x => x._1)

object FSM:
  def id[S, I, O](run: (S, I) => Task[(S, O)]) = FSM(run)
