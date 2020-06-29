package rest.controllers

import ar.com.flow.bikerental.model.token.{ReservedRentToken, TokenRegistry}
import ar.com.flow.bikerental.model.{User, UserRepository}
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc.{BaseController, ControllerComponents}
import rest.controllers.RentTokenJsonMappers._
import rest.controllers.UserJsonMappers.{userWrites, _}

@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents, val userRepository: UserRepository, val tokenRegistry: TokenRegistry) extends BaseController {
  def create() = Action(parse.json[User]) { request =>
    val user = userRepository.save(request.body)
    Created(Json.toJson(user))
  }

  def retrieve(userId: Long) = getForUser[User](userId)(identity)

  def reserveTokenForUser(userId: Long) =
    getForUser[ReservedRentToken](userId)(tokenRegistry.reserveTokenForUser(_))

  private def getForUser[T](userId: Long)(f: User => T)(implicit writes: Writes[T]) = Action {
    userRepository.getById(userId)
      .map(f)
      .map(result => Ok(Json.toJson(result)(writes)))
      .getOrElse(NotFound(userId.toString))
  }
}

object UserJsonMappers {
  implicit val userReads: Reads[User] = Json.reads[User]
  implicit val userWrites: Writes[User] = Json.writes[User]
}

object RentTokenJsonMappers {
  implicit val rentTokenReads = Json.reads[ReservedRentToken]

  implicit val reservedRentTokenWrites: Writes[ReservedRentToken] =
    (o: ReservedRentToken) => Json.obj(
      "value" -> o.value,
      "expiration" -> o.expiration,
      "owner" -> o.owner
    )
}