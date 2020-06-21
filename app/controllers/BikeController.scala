package controllers

import ar.com.flow.bikerental.model.{Bike, BikeRepository}
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AnyContent, BaseController, ControllerComponents, Request}
import play.api.libs.json._

@Singleton
class BikeController @Inject()(val controllerComponents: ControllerComponents, val bikeRepository: BikeRepository) extends BaseController {
  def create(serialNumber: String) = Action { implicit request: Request[AnyContent] =>
    bikeRepository.save(Bike(serialNumber))
    Created(serialNumber)
  }

  def retrieve(serialNumber: String) = Action { implicit request: Request[AnyContent] =>
    implicit val bikeWrites = Json.writes[Bike]

    bikeRepository.getBySerialNumber(serialNumber)
      .map(bike => Ok(Json.toJson(bike)))
      .getOrElse(NotFound(serialNumber))
  }
}
