package controllers

import ar.com.flow.bikerental.model.{Bike, BikeRepository}
import controllers.JsonMappers._
import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc.{BaseController, ControllerComponents}

@Singleton
class BikeController @Inject()(val controllerComponents: ControllerComponents, val bikeRepository: BikeRepository) extends BaseController {
  def create() = Action(parse.json[Bike]) { request =>
    bikeRepository.save(request.body)
    Created(request.body.serialNumber)
  }

  def retrieve(serialNumber: String) = Action {
    bikeRepository.getBySerialNumber(serialNumber)
      .map(bike => Ok(Json.toJson(bike)))
      .getOrElse(NotFound(serialNumber))
  }
}
