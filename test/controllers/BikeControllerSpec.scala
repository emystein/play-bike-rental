package controllers

import ar.com.flow.bikerental.model.Bike
import controllers.JsonMappers._
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class BikeControllerSpec extends PlaySpec with GuiceOneAppPerTest {
  val bike = Bike("1")

  "BikeController" should {
    "create and retrieve bike" in {
      create(bike)

      val retrievedBike = retrieveBike(bike.serialNumber)

      retrievedBike mustBe bike
    }
  }

  private def create(bike: Bike): Unit = {
    val request = FakeRequest(POST, s"/bikes/${bike.serialNumber}").withJsonBody(Json.toJson(bike))
    val response = route(app, request).get
    status(response) mustBe CREATED
  }
  
  private def retrieveBike(serialNumber: String): Bike = {
    val request = FakeRequest(GET, s"/bikes/$serialNumber")
    val response = route(app, request).get
    status(response) mustBe OK
    contentAsJson(response).as[Bike]
  }
}
