package rest.controllers

import java.time.LocalDateTime

import ar.com.flow.bikerental.model.{User, UserRepository}
import ar.com.flow.bikerental.model.token.{ReservedRentToken, TokenRegistry}
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc.{Action, BaseController, ControllerComponents, Result}
import rest.controllers.UserJsonMappers.{userWrites, _}
import UserJsonMappers._
import RentTokenJsonMappers._

@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents, val userRepository: UserRepository, val tokenRegistry: TokenRegistry) extends BaseController {
  def create() = Action(parse.json[User]) { request =>
    val user: User = userRepository.save(request.body)
    Created(Json.toJson(user))
  }

  def retrieve(userId: Long) = Action {
    userRepository.getById(userId)
      .map(user => Ok(Json.toJson(user)))
      .getOrElse(NotFound(userId.toString))
  }

  def reserveTokenForUser(userId: Long) = Action {
    userRepository.getById(userId)
      .map(user => tokenRegistry.reserveTokenForUser(user))
      .map(reservedToken => Ok(Json.toJson(reservedToken)))
      .getOrElse(NotFound(userId.toString))
  }
}

object UserJsonMappers {
  implicit val userReads: Reads[User] = Json.reads[User]
  implicit val userWrites: Writes[User] = Json.writes[User]
}

case class ReservedRentTokenDto(value: String, expiration: LocalDateTime, owner: User)

object RentTokenJsonMappers {
  implicit val rentTokenReads = Json.reads[ReservedRentTokenDto]

  implicit val reservedRentTokenWrites: Writes[ReservedRentToken] =
    (o: ReservedRentToken) => Json.obj(
      "value" -> o.value,
      "expiration" -> o.expiration,
      "owner" -> o.owner
    )
}