package controllers

import ar.com.flow.bikerental.model.BikeRepository
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AnyContent, BaseController, ControllerComponents, Request}

@Singleton
class BikeController @Inject()(val controllerComponents: ControllerComponents, val bikeRepository: BikeRepository) extends BaseController {
  def create(serialNumber: String) = Action { implicit request: Request[AnyContent] =>
    Ok("Created bike with serial number: " + serialNumber)
  }
}
