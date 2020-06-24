package controllers

import ar.com.flow.bikerental.model.User
import ar.com.flow.bikerental.model.token._
import controllers.JsonMappers._
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class UserControllerSpec extends PlaySpec with GuiceOneAppPerTest with BeforeAndAfterEach {
  val user = User("1", "emystein")

  "UserController" should {
    "create and retrieve user" in {
      create(user)

      val retrievedUser = retrieveUser(user.id)

      retrievedUser mustBe user
    }
    "reserve token" in {
      create(user)

      val token = reserveToken(user)
    }
  }

  private def reserveToken(user: User): RentToken = {
    val response = route(app, FakeRequest(GET, s"/users/${user.id}/rent-token")).get
    status(response) mustBe OK
    contentAsJson(response).as[RentToken]
  }

  private def retrieveUser(userId: String): User = {
    val request = FakeRequest(GET, s"/users/$userId")
    val response = route(app, request).get
    status(response) mustBe OK
    contentAsJson(response).as[User]
  }

  private def create(user: User): Unit = {
    val request = FakeRequest(POST, "/users").withJsonBody(Json.toJson(user))
    val response = route(app, request).get
    status(response) mustBe CREATED
  }
}
