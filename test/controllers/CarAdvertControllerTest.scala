package controllers

import java.util.concurrent.TimeUnit

import akka.util.Timeout
import org.scalatestplus.play._
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest}

import scala.concurrent.{ExecutionContext, Future}


class CarAdvertControllerTest extends PlaySpec with Results with OneAppPerSuite {
  "test create" should {
    "should create a new car advert" in {
      //Given
      implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

      def mockApp: Application = new GuiceApplicationBuilder().build()

      val fakeRequest = FakeRequest(POST, "/car/adverts", FakeHeaders(),
        AnyContentAsJson(Json.parse("""{"title": "test","fuel": "Gasoline","price": 10,"new": true,"mileage": 0,"first_registration": "2017-11-04"}""")))
      //When
      val futureResult: Future[Result] = route(mockApp, fakeRequest).get

      //Then
      val resultJson: JsValue = contentAsJson(futureResult)(Timeout(2, TimeUnit.SECONDS))
      resultJson.toString contains """{"title": "test"}"""
    }
  }

  "test showAll" should {
    "should show all car adverts" in {
      //Given
      implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

      def mockApp: Application = new GuiceApplicationBuilder().build()

      val fakePostRequest = FakeRequest(POST, "/car/adverts", FakeHeaders(),
        AnyContentAsJson(Json.parse("""{"title": "test","fuel": "Gasoline","price": 10,"new": true,"mileage": 0,"first_registration": "2017-11-04"}""")))
      route(mockApp, fakePostRequest)

      //When
      val fakeGetRequest = FakeRequest(GET, "/car/adverts", FakeHeaders(), AnyContentAsEmpty)

      //Then
      val futureResult: Future[Result] = route(mockApp, fakeGetRequest).get
      val resultJson: JsValue = contentAsJson(futureResult)(Timeout(2, TimeUnit.SECONDS))
      resultJson.toString contains """{"title": "test"}"""
    }
  }
}
