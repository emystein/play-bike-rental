package controllers

import ar.com.flow.bikerental.model.{Bike, BikeStationRepository}
import ar.com.flow.bikerental.model.token.{ReservedRentToken, TokenRegistry}
import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc._
import controllers.JsonMappers._

@Singleton
class BikeStationController @Inject()(val controllerComponents: ControllerComponents, val tokenRegistry: TokenRegistry, val stationRepository: BikeStationRepository) extends BaseController {
  def pickupBike(stationId: String, anchorageId: Int, tokenValue: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
      val bike: Option[Bike] = for {
        reservedToken <- tokenRegistry.getTokenByValue(tokenValue)
        station <- stationRepository.getById(stationId)
        anchorage <- station.getAnchorageById(anchorageId)
        bike <- anchorage.releaseBike(reservedToken.asInstanceOf[ReservedRentToken])
      } yield bike

      bike.map(bike => Ok(Json.toJson(bike))).getOrElse(NotFound(tokenValue))
  }
}
