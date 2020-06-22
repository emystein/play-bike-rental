package controllers

import ar.com.flow.bikerental.model.token.{ReservedRentToken, TokenRegistry}
import ar.com.flow.bikerental.model.{User, UserRepository}
import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc._
import JsonMappers._

@Singleton
class RentTokenRegistryController @Inject()(val controllerComponents: ControllerComponents, val userRepository: UserRepository, val tokenRegistry: TokenRegistry) extends BaseController {
  def getNewToken(userId: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    userRepository.getById(userId)
      .map(user => tokenRegistry.reserveTokenForUser(user))
      .map(reservedToken => Ok(Json.toJson(reservedToken)))
      .getOrElse(NotFound(userId))
  }
}
