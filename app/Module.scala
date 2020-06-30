import ar.com.flow.bikerental.model.token.{ConsumedRentToken, TokenRegistry, TokenRepository, _}
import ar.com.flow.bikerental.model.trip.completion.{TripCompletionRules, TripCompletionRulesFactory}
import ar.com.flow.bikerental.model.{BikeRepository, BikeShop, BikeStationRepository, InMemoryBikeRepository, InMemoryBikeStationRepository, InMemoryUserRepository, TripRegistry, UserRepository}
import com.google.inject.{AbstractModule, TypeLiteral}
import com.google.inject.name.Names
import javax.inject.Named
import persistence.{AnormBikeRepository, AnormReservedRentTokenRepository, AnormUserRepository}
import play.api.{Configuration, Environment}
import play.api.db._

import scala.util.Random

class Module(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[UserRepository]).to(classOf[AnormUserRepository]).asEagerSingleton()

    bind(classOf[BikeRepository]).to(classOf[AnormBikeRepository]).asEagerSingleton()

//    bind(new TypeLiteral[TokenRepository[ReservedRentToken]] {}).toInstance(reservedRentTokenRepository)

//    val consumedRentTokenRepository = new InMemoryConsumedRentTokenRepository()
//    bind(new TypeLiteral[TokenRepository[ConsumedRentToken]] {}).toInstance(consumedRentTokenRepository)

//    val tokenRegistry = TokenRegistry(new Random(), reservedRentTokenRepository, consumedRentTokenRepository)
//    bind(classOf[TokenRegistry]).toInstance(tokenRegistry)
//
//
//    val tripRegistry = TripRegistry(tokenRegistry, TripCompletionRulesFactory.create)
//    bind(classOf[TripRegistry]).toInstance(tripRegistry)
    bind(classOf[BikeStationRepository]).to(classOf[InMemoryBikeStationRepository]).asEagerSingleton()

    val bikeShop = new BikeShop()
    bind(classOf[BikeShop]).toInstance(bikeShop)
  }

  import com.google.inject.Provides

  @Provides
  def reservedRentTokenRepository(db: Database): TokenRepository[ReservedRentToken] =
    new AnormReservedRentTokenRepository(db)

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