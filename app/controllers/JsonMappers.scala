package controllers

import java.time.LocalDateTime

import ar.com.flow.bikerental.model.{Bike, User}
import ar.com.flow.bikerental.model.token.ReservedRentToken
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

object JsonMappers {
  implicit val userReads: Reads[User] = Json.reads[User]
  implicit val userWrites: Writes[User] = Json.writes[User]

  implicit val bikeReads: Reads[Bike] = Json.reads[Bike]
  implicit val bikeWrites: Writes[Bike] = Json.writes[Bike]

  implicit val rentTokenReads = Json.reads[ReservedRentTokenDto]

  implicit val reservedRentTokenWrites: Writes[ReservedRentToken] =
    (o: ReservedRentToken) => Json.obj(
      "value" -> o.value,
      "expiration" -> o.expiration,
      "owner" -> o.owner
    )
}

