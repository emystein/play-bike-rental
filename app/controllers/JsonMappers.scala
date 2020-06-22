package controllers

import ar.com.flow.bikerental.model.User
import ar.com.flow.bikerental.model.token.ReservedRentToken
import play.api.libs.json.{Json, Reads, Writes}

object JsonMappers {
  implicit val userReads: Reads[User] = Json.reads[User]
  implicit val userWrites: Writes[User] = Json.writes[User]

  implicit val reservedRentTokenWrites: Writes[ReservedRentToken] =
    (o: ReservedRentToken) => Json.obj(
      "value" -> o.value,
      "expiration" -> o.expiration,
      "owner" -> o.owner
    )
}

