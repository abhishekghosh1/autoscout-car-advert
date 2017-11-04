package services.validation

import models.Fuel

import scala.util.{Failure, Success, Try}

trait ValidationTrait {

  def normalizeId(id: Option[String]): String = id.getOrElse(generateUuid())

  def validateFuel(s: String): Try[Unit]      = if (Fuel.isFuelType(s)) Success(Unit) else Failure (new Throwable("Fuel type is invalid"))
  def validateTitle(s: String): Try[Unit]     = if(s.length > 0) Success(Unit) else Failure(new Throwable("Title must not be empty"))
  def validatePrice(price: Int): Try[Unit]    = if (price > 0) Success(Unit) else Failure (new Throwable("Price should be greater than 0"))

  private def generateUuid(): String = java.util.UUID.randomUUID.toString


}

