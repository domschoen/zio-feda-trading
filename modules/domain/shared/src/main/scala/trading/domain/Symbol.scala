package trading.domain

import zio.schema.*

final case class Symbol(value: String)

object Symbol {
  val CHFEUR = Symbol("CHFEUR")
  val EURPLN = Symbol("EURPLN")
  val EURUSD = Symbol("EURUSD")
  val GBPUSD = Symbol("GBPUSD")
  val AUDCAD = Symbol("AUDCAD")
  val USDCAD = Symbol("USDCAD")
  val CHFGBP = Symbol("CHFGBP")
  val XEMPTY = Symbol("XXXXXX")

  def isValidSymbol(value: String): Boolean =
    """^[a-zA-Z0-9]{6}$""".r.findFirstIn(value).isDefined

  def fromString(value: String): Either[String, Symbol] =
    if (isValidSymbol(value)) Right(Symbol(value))
    else Left(s"Invalid symbol: $value. A Symbol should be an alphanumeric of 6 digits")

  given schema: Schema[Symbol] =
    // first arg: from String to Symbol,
    // second arg: from Symbol to String
    Schema[String].transformOrFail(fromString(_), symbol => Right(symbol.value))

  // put error inside schema
}
