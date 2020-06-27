package rest.controllers

import ar.com.flow.bikerental.model.User
import org.scalatest.MustMatchers
import play.api.Application
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, POST, contentAsJson, route, status, _}
import rest.controllers.RentTokenJsonMappers._
import rest.controllers.UserJsonMappers._

object UserApi extends MustMatchers {
  def createUser(user: User)(implicit app: Application): User = {
    val request = FakeRequest(POST, "/users").withJsonBody(Json.toJson(user))
    val response = route(app, request).get
    status(response) mustBe CREATED
    contentAsJson(response).as[User]
  }

  def retrieveUser(userId: Option[Long])(implicit app: Application): User = {
    val request = FakeRequest(GET, s"/users/${userId.get}")
    val response = route(app, request).get
    status(response) mustBe OK
    contentAsJson(response).as[User]
  }

  def reserveToken(user: User)(implicit app: Application): ReservedRentTokenDto = {
    val response = route(app, FakeRequest(GET, s"/users/${user.id.get}/rent-token")).get
    status(response) mustBe OK
    contentAsJson(response).as[ReservedRentTokenDto]
  }
}
