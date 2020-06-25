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

  def create(): Action[BikeStationDto] = Action(parse.json[BikeStationDto]) { request =>
    val bikeStation = new BikeStation(request.body.id, request.body.numberOfBikeAnchorages.toInt, tripRegistry, bikeShop)
    bikeStationRepository.save(bikeStation)
    Created(request.body.id)
  }

  def retrieve(id: String): Action[AnyContent] = Action { request =>
    bikeStationRepository.getById(id)
      .map(bikeStation => BikeStationDto(bikeStation.id, bikeStation.numberOfBikeAnchorages.toString))
      .map(bikeStation => Ok(Json.toJson(bikeStation)))
      .getOrElse(NotFound(id))
  }

  def pickupBike(stationId: String, anchorageId: Int, tokenValue: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
      val bike: Option[Bike] = for {
        reservedToken <- tokenRegistry.getTokenByValue(tokenValue)
        station <- bikeStationRepository.getById(stationId)
        anchorage <- station.getAnchorageById(anchorageId)
        bike <- anchorage.releaseBike(reservedToken.asInstanceOf[ReservedRentToken])
      } yield bike

      bike.map(bike => Ok(Json.toJson(bike))).getOrElse(NotFound(tokenValue))
  }
}

case class BikeStationDto(id: String, numberOfBikeAnchorages: String)

object BikeStationJsonMappers {
  implicit val bikeStationDtoReads: Reads[BikeStationDto] = Json.reads[BikeStationDto]
  implicit val bikeStationDtoWrites: Writes[BikeStationDto] = Json.writes[BikeStationDto]
}
