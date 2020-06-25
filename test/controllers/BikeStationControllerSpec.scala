package controllers

import controllers.BikeStationJsonMappers._
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class BikeStationControllerSpec extends PlaySpec with GuiceOneAppPerTest {
  "BikeStationController" should {
    "create and retrieve station" in {
      create(stationId = "1", numberOfBikeAnchorages = 10)

      val bikeStation = retrieveBikeStation("1")

      bikeStation.id mustBe "1"
      bikeStation.numberOfBikeAnchorages mustBe "10"
    }
  }

  private def create(stationId: String, numberOfBikeAnchorages: Int): Unit = {
    val bikeStation = BikeStationDto(stationId, numberOfBikeAnchorages.toString)
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
}
