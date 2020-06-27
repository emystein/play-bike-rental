package rest.controllers

import ar.com.flow.bikerental.model.User
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import rest.controllers.UserApi._

class UserControllerSpec extends PlaySpec with GuiceOneAppPerTest {
  val user = User(id = None, name = "emystein")

  "UserController" should {
    "create and retrieve user" in {
      val createdUser = createUser(user)

      val retrievedUser = retrieveUser(createdUser.id)

      retrievedUser.name mustBe createdUser.name
    }
    "reserve token" in {
      val createdUser = createUser(user)

      val token = reserveToken(createdUser)

      token.owner.name mustBe createdUser.name
      token.value mustNot be(null)
      token.expiration mustNot be(null)
    }
  }
}

