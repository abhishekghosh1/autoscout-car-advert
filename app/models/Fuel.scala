package models


object Fuel extends Enumeration {
  type Fuel = Value
  val Gasoline = Value("Gasoline")
  val Diesel = Value("Diesel")

  def fromString(s: String): Option[Fuel.Value] = values.find(_.toString == s)

  def isFuelType(s: String): Boolean = fromString(s).isDefined

}
