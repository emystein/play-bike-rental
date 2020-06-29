package persistence

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.SECONDS

import ar.com.flow.bikerental.model.User
import ar.com.flow.bikerental.model.token.ReservedRentToken
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest

class AnormReservedTokenRepositorySpec extends PlaySpec with GuiceOneAppPerTest {
  "AnormReservedRentTokenRepository" should {
    "create and retrieve token" in {
      val owner = persistUser

      val repository = app.injector.instanceOf(classOf[AnormReservedRentTokenRepository])

      val token = ReservedRentToken("1", LocalDateTime.now().plusDays(1), owner, tokenRegistry = null)

      repository.save(token)

      val retrievedToken = repository.getById(token.value).get

      retrievedToken.value mustBe token.value
      retrievedToken.owner mustBe token.owner
      retrievedToken.expiration.truncatedTo(SECONDS) mustBe token.expiration.truncatedTo(SECONDS)
    }
    "get all tokens" in {
      val owner = persistUser

      val repository = app.injector.instanceOf(classOf[AnormReservedRentTokenRepository])

      val tokenExpiration = LocalDateTime.now().plusDays(1)

      val token1 = ReservedRentToken("1", tokenExpiration, owner, tokenRegistry = null)
      val token2 = ReservedRentToken("2", tokenExpiration, owner, tokenRegistry = null)

      val allTokens = List(token1, token2)

      allTokens.foreach(token => repository.save(token))

      repository.getAll() must contain theSameElementsAs allTokens
    }
    "get all tokens by user" in {
      val owner = persistUser

      val repository = app.injector.instanceOf(classOf[AnormReservedRentTokenRepository])

      val tokenExpiration = LocalDateTime.now().plusDays(1)

      val token1 = ReservedRentToken("1", tokenExpiration, owner, tokenRegistry = null)
      val token2 = ReservedRentToken("2", tokenExpiration, owner, tokenRegistry = null)
      val tokenOwnedByOther = ReservedRentToken("3", tokenExpiration, User(Some(owner.id.get + 1), "Other"), tokenRegistry = null)

      val allReservedRentTokens = List(token1, token2, tokenOwnedByOther)

      allReservedRentTokens.foreach(token => repository.save(token))

      repository.getAllByUser(owner) must contain theSameElementsAs List(token1, token2)
    }
  }

  private def persistUser = {
    val user = User(Some(1), "emystein")
    val userRepository = app.injector.instanceOf(classOf[AnormUserRepository])
    userRepository.save(user)
    user
  }
}
