package rest.controllers

import ar.com.flow.bikerental.model.User
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import UserJsonMappers._
import RentTokenJsonMappers._

class UserControllerSpec extends PlaySpec with GuiceOneAppPerTest {
  val user = User(id = None, name = "emystein")

  "UserController" should {
    "create and retrieve user" in {
      val createdUser = create(user)

      val retrievedUser = retrieveUser(createdUser.id)

      retrievedUser.name mustBe createdUser.name
    }
    "reserve token" in {
      val createdUser = create(user)

      val token = reserveToken(createdUser)

      token.owner.name mustBe createdUser.name
      token.value mustNot be(null)
      token.expiration mustNot be(null)
    }
  }

  private def reserveToken(user: User): ReservedRentTokenDto = {
    val response = route(app, FakeRequest(GET, s"/users/${user.id.get}/rent-token")).get
    status(response) mustBe OK
    contentAsJson(response).as[ReservedRentTokenDto]
  }

  private def retrieveUser(userId: Option[String]): User = {
    val request = FakeRequest(GET, s"/users/${userId.get}")
    val response = route(app, request).get
    status(response) mustBe OK
    contentAsJson(response).as[User]
  }

  private def create(user: User): User = {
    val request = FakeRequest(POST, "/users").withJsonBody(Json.toJson(user))
    val response = route(app, request).get
    status(response) mustBe CREATED
    contentAsJson(response).as[User]
  }
}
