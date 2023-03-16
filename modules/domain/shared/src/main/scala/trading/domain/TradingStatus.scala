package trading.domain

import zio.optics.*
import zio.json.*
import zio.schema.*
import zio.schema.Schema.Field

import java.time.Instant
import java.util.UUID

enum TradingStatus:
  case On
  case Off

object TradingStatus {
  def fromString(str: String): Either[String, TradingStatus] =
    try {
      Right(valueOf(str))
    } catch {
      case t if scala.util.control.NonFatal(t) => Left(t.toString)
    }

  given schema: Schema[TradingStatus] =
    Schema[String].transformOrFail(fromString(_), status => Right(status.toString))

}