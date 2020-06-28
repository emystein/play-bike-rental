package rest.controllers

import ar.com.flow.bikerental.model._
import ar.com.flow.bikerental.model.token.{ReservedRentToken, TokenRegistry, TokenRepository}
import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc._
import rest.controllers.BikeJsonMappers._
import rest.controllers.BikeStationJsonMappers._

@Singleton
class BikeStationController @Inject()(val controllerComponents: ControllerComponents,
                                      val tokenRegistry: TokenRegistry,
                                      val reservedRentTokenRepository: TokenRepository[ReservedRentToken],
                                      val bikeStationRepository: BikeStationRepository,
                                      val tripRegistry: TripRegistry,
                                      val bikeShop: BikeShop) extends BaseController {

  def create() = Action(parse.json[BikeStationDto]) { request =>
    val bikeStation = BikeStation(request.body.id, request.body.anchorageCount.toInt, tripRegistry, bikeShop)
    val createdBikeStation = bikeStationRepository.save(bikeStation)
    Created(Json.toJson(createdBikeStation.id))
  }

  def retrieve(id: String) = Action {
    bikeStationRepository.getById(id)
      .map(bikeStation => BikeStationDto(bikeStation.id, bikeStation.anchorageCount.toString))
      .map(bikeStation => Ok(Json.toJson(bikeStation)))
      .getOrElse(NotFound(id))
  }

  def parkBike(stationId: String, anchorageId: Int) = Action(parse.json[Bike]) { request =>
    val maybeBikeStation = for {
      station <- bikeStationRepository.getById(stationId)
      anchorage <- station.getAnchorageById(anchorageId)
      _ = anchorage.parkBike(request.body)
    } yield {
      station
    }

    maybeBikeStation.map(s => Ok(Json.toJson(BikeStationDto(s.id, s.anchorageCount.toString)))).getOrElse(NotFound)
  }

  def pickupBike(stationId: String, anchorageId: Int, rentToken: Option[String]) = Action {
      val bike: Option[Bike] = for {
        station <- bikeStationRepository.getById(stationId)
        anchorage <- station.getAnchorageById(anchorageId)
        rentTokenValue <- rentToken
        reservedToken <- reservedRentTokenRepository.getById(rentTokenValue)
        bike <- anchorage.releaseBike(reservedToken.asInstanceOf[ReservedRentToken])
      } yield bike

      bike.map(bike => Ok(Json.toJson(bike))).getOrElse(NotFound)
  }
}

// TODO change anchorageCount type to Int
case class BikeStationDto(id: Option[String], anchorageCount: String)

case class BikePickUpRequest(rentToken: String)

object BikeStationJsonMappers {
  implicit val bikeStationDtoReads: Reads[BikeStationDto] = Json.reads[BikeStationDto]
  implicit val bikeStationDtoWrites: Writes[BikeStationDto] = Json.writes[BikeStationDto]
  
  implicit val bikePickUpRequestReads: Reads[BikePickUpRequest] = Json.reads[BikePickUpRequest]
  implicit val bikePickUpRequestWrites: Writes[BikePickUpRequest] = Json.writes[BikePickUpRequest]
}
