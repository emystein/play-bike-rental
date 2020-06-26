package controllers

import ar.com.flow.bikerental.model.token.{ReservedRentToken, TokenRegistry}
import ar.com.flow.bikerental.model._
import controllers.BikeStationJsonMappers._
import controllers.JsonMappers._
import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc._

@Singleton
class BikeStationController @Inject()(val controllerComponents: ControllerComponents,
                                      val tokenRegistry: TokenRegistry,
                                      val bikeStationRepository: BikeStationRepository,
                                      val tripRegistry: TripRegistry,
                                      val bikeShop: BikeShop) extends BaseController {

  def create() = Action(parse.json[BikeStationDto]) { request =>
    val bikeStation = new BikeStation(request.body.id, request.body.numberOfBikeAnchorages.toInt, tripRegistry, bikeShop)
    bikeStationRepository.save(bikeStation)
    Created(request.body.id)
  }

  def retrieve(id: String) = Action {
    bikeStationRepository.getById(id)
      .map(bikeStation => BikeStationDto(bikeStation.id, bikeStation.numberOfBikeAnchorages.toString))
      .map(bikeStation => Ok(Json.toJson(bikeStation)))
      .getOrElse(NotFound(id))
  }

  def parkBike(stationId: String, anchorageId: Int, bikeSerialNumber: Option[String]) = Action {
    val maybeBikeStation: Option[BikeStation] = for {
      station <- bikeStationRepository.getById(stationId)
      bikeSerialNumber <- bikeSerialNumber
    } yield {
      station.parkBikeAtAnchorage(Bike(bikeSerialNumber), anchorageId)
    }

    maybeBikeStation.map(s => Ok(Json.toJson(BikeStationDto(s.id, s.numberOfBikeAnchorages.toString)))).getOrElse(NotFound)
  }

  def pickupBike(stationId: String, anchorageId: Int, rentToken: Option[String]) = Action {
      val bike: Option[Bike] = for {
        station <- bikeStationRepository.getById(stationId)
        anchorage <- station.getAnchorageById(anchorageId)
        rentTokenValue <- rentToken
        reservedToken <- tokenRegistry.getTokenByValue(rentTokenValue)
        bike <- anchorage.releaseBike(reservedToken.asInstanceOf[ReservedRentToken])
      } yield bike

      bike.map(bike => Ok(Json.toJson(bike))).getOrElse(NotFound)
  }
}

case class BikeStationDto(id: String, numberOfBikeAnchorages: String)

case class BikePickUpRequest(rentToken: String)

object BikeStationJsonMappers {
  implicit val bikeStationDtoReads: Reads[BikeStationDto] = Json.reads[BikeStationDto]
  implicit val bikeStationDtoWrites: Writes[BikeStationDto] = Json.writes[BikeStationDto]
  
  implicit val bikePickUpRequestReads: Reads[BikePickUpRequest] = Json.reads[BikePickUpRequest]
  implicit val bikePickUpRequestWrites: Writes[BikePickUpRequest] = Json.writes[BikePickUpRequest]
}
