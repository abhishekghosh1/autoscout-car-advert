package controllers

import javax.inject._

import play.api.libs.json.{JsValue, Json, JsString}
import play.api.mvc.{Action, _}
import services.storage.StorageDriverTrait

import scala.util.Try
import services.json.{Converter => JsonConverter}
import services.storage.{Converter => StorableConverter}

/**
 * Cart adverts CRUD controller
 */
@Singleton
class CarAdvertController @Inject()(storage: StorageDriverTrait) extends Controller {

  def showAll(sortBy: Option[String] = Some("id")): Action[AnyContent]  = Action {

    // Since it's quite hard handle all Try's until the very end, we wrap the call in a Try
    // and let the inner Trys rethrow when accessing them unsafely (.get)
    val operation: Try[Seq[JsValue]] = Try {
      storage
        .showAll()
        .get
        .map(StorableConverter.toAdvert)
        .map(_.get)
        .sortBy(advert => advert.fold(_.id, _.id))
        .map(_.fold(_.toJson, _.toJson))
    }

    operation
      .map(Json.toJson(_))
      .map(Ok(_))
      .getOrElse(
        BadRequest(JsString(operation.failed.get.getMessage))
      )
  }

  def show(id: String): Action[AnyContent] = Action {
    val operation = storage
      .show(id)
      .flatMap(StorableConverter.toAdvert)
      .map(_.fold(_.toJson, _.toJson))

    operation
      .map(Ok(_))
      .getOrElse(
        NotFound(JsString(operation.failed.get.getMessage))       // We'll assume exceptions are due to 404, so use 404 status
      )
  }

  def create: Action[JsValue] = Action(parse.json) { request =>
    val operation = JsonConverter
      .toAdvert(request.body)
      .map(_.fold(storage.create(_), storage.create(_)))
      .flatMap(x => x)
      .flatMap(StorableConverter.toAdvert)
      .map(_.fold(_.toJson, _.toJson))

    operation
      .map(x => Ok(x))
      .getOrElse(
        BadRequest(JsString(operation.failed.get.getMessage))
      )
  }

  def update(id: String): Action[JsValue] = Action(parse.json) { request =>
    val operation = JsonConverter
      .toAdvert(request.body, Some(id))
      .map(_.fold(storage.update(_), storage.update(_)))
      .flatMap(x => x)
      .flatMap(StorableConverter.toAdvert)
      .map(_.fold(_.toJson, _.toJson))

    operation
      .map(x => Ok(x))
      .getOrElse(
        BadRequest(JsString(operation.failed.get.getMessage))
      )
  }

  def delete(id: String): Action[AnyContent] = Action {
    val operation = storage.delete(id)

    operation
      .map(x => Ok(JsString("")))
      .getOrElse(
        BadRequest(JsString(operation.failed.get.getMessage))
      )
  }

}
