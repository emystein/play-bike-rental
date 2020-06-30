import ar.com.flow.bikerental.model.token.{ConsumedRentToken, TokenRegistry, TokenRepository, _}
import ar.com.flow.bikerental.model.trip.completion.TripCompletionRulesFactory
import ar.com.flow.bikerental.model._
import com.google.inject.{AbstractModule, Provides, TypeLiteral}
import persistence.{AnormBikeRepository, AnormReservedRentTokenRepository, AnormUserRepository}
import play.api.db._
import play.api.{Configuration, Environment}

import scala.util.Random

class Module(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[UserRepository]).to(classOf[AnormUserRepository]).asEagerSingleton()

    bind(classOf[BikeRepository]).to(classOf[AnormBikeRepository]).asEagerSingleton()

    bind(classOf[BikeStationRepository]).to(classOf[InMemoryBikeStationRepository]).asEagerSingleton()

    bind(classOf[BikeShop]).toInstance(new BikeShop())

    bind(new TypeLiteral[TokenRepository[ReservedRentToken]] {}).to(classOf[AnormReservedRentTokenRepository])
  }

  @Provides
  def consumedRentTokenRepository(): TokenRepository[ConsumedRentToken] =
    new InMemoryConsumedRentTokenRepository()

  @Provides
  def tokenRegistry(reservedRentTokenRepository: TokenRepository[ReservedRentToken],
                    consumedRentTokenRepository: TokenRepository[ConsumedRentToken]): TokenRegistry =
    TokenRegistry(new Random(), reservedRentTokenRepository, consumedRentTokenRepository)

  @Provides
  def tripRegistry(tokenRegistry: TokenRegistry): TripRegistry =
    TripRegistry(tokenRegistry, TripCompletionRulesFactory.create)
}