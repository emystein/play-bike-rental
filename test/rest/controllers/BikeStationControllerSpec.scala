package rest.controllers

import ar.com.flow.bikerental.model.{Bike, User}
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import rest.controllers.UserApi._
import rest.controllers.BikeJsonMappers._
import rest.controllers.BikeStationJsonMappers._

class BikeStationControllerSpec extends PlaySpec with GuiceOneAppPerTest {
  "BikeStationController" should {
    "create and retrieve station" in {
      create(bikeStationId = "1", anchorageCount = 10)

      val bikeStation = retrieveBikeStation("1")

      bikeStation.id mustBe Some("1")
      bikeStation.anchorageCount mustBe "10"
    }
    "park bike" in {
      create(bikeStationId = "1", anchorageCount = 10)

      parkBike(bikeStationId = "1", anchorageId = 1, bikeSerialNumber = "1")
    }
    "pickup bike" in {
      val user = User(Some(1), "emystein")
      createUser(user)
      val rentToken = reserveToken(user)
      create(bikeStationId = "1", anchorageCount = 10)
      parkBike(bikeStationId = "1", anchorageId = 1, bikeSerialNumber = "1")

      val bike = pickUpBike(bikeStationId = "1", anchorageId = 1, rentToken.value)

      bike mustNot be(null)
    }
  }

  private def create(bikeStationId: String, anchorageCount: Int): Unit = {
    val bikeStation = BikeStationDto(Some(bikeStationId), anchorageCount.toString)
    val request = FakeRequest(POST, "/bike-stations").withJsonBody(Json.toJson(bikeStation))
    val response = route(app, request).get
    status(response) mustBe CREATED
  }

  private def retrieveBikeStation(bikeStationId: String): BikeStationDto = {
    val request = FakeRequest(GET, s"/bike-stations/$bikeStationId")
    val response = route(app, request).get
    status(response) mustBe OK
    contentAsJson(response).as[BikeStationDto]
  }

  private def parkBike(bikeStationId: String, anchorageId: Int, bikeSerialNumber: String): BikeStationDto = {
    val bike = Bike( bikeSerialNumber)
    val request = FakeRequest(POST, s"/bike-stations/$bikeStationId/anchorages/$anchorageId/bike")
      .withJsonBody(Json.toJson(bike))
    val response = route(app, request).get
    status(response) mustBe OK
    contentAsJson(response).as[BikeStationDto]
  }

  private def pickUpBike(bikeStationId: String, anchorageId: Int, rentToken: String): Bike = {
    val request = FakeRequest(POST, s"/bike-stations/$bikeStationId/anchorages/$anchorageId/bike/pickup?rentToken=$rentToken")
    val response = route(app, request).get
    status(response) mustBe OK
    contentAsJson(response).as[Bike]
  }
}
