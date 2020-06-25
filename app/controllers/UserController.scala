package controllers

import java.time.LocalDateTime

import ar.com.flow.bikerental.model.token.TokenRegistry
import ar.com.flow.bikerental.model.{User, UserRepository}
import controllers.JsonMappers._
import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc._

@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents, val userRepository: UserRepository, val tokenRegistry: TokenRegistry) extends BaseController {
  def create(): Action[User] = Action(parse.json[User]) { request =>
    userRepository.save(request.body)
    Created(request.body.id)
  }

  def retrieve(serialNumber: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    userRepository.getById(serialNumber)
      .map(user => Ok(Json.toJson(user)))
      .getOrElse(NotFound(serialNumber))
  }

  def reserveTokenForUser(userId: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    userRepository.getById(userId)
      .map(user => tokenRegistry.reserveTokenForUser(user))
      .map(reservedToken => Ok(Json.toJson(reservedToken)))
      .getOrElse(NotFound(userId))
  }
}

case class ReservedRentTokenDto(value: String, expiration: LocalDateTime, owner: User)
