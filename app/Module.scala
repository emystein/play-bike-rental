import ar.com.flow.bikerental.model.token._
import ar.com.flow.bikerental.model.trip.completion.{TripCompletionRules, TripCompletionRulesFactory}
import ar.com.flow.bikerental.model.{BikeRepository, BikeShop, BikeStationRepository, InMemoryBikeRepository, InMemoryBikeStationRepository, InMemoryUserRepository, TripRegistry, UserRepository}
import com.google.inject.{AbstractModule, TypeLiteral}
import com.google.inject.name.Names
import javax.inject.Named
import persistence.{AnormBikeRepository, AnormUserRepository}

import scala.util.Random

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[UserRepository]).to(classOf[AnormUserRepository]).asEagerSingleton()

    bind(classOf[BikeRepository]).to(classOf[AnormBikeRepository]).asEagerSingleton()

    val reservedRentTokenRepository = new InMemoryReservedRentTokenRepository()
    bind(new TypeLiteral[TokenRepository[ReservedRentToken]] {}).toInstance(reservedRentTokenRepository)

    val consumedRentTokenRepository = new InMemoryConsumedRentTokenRepository()
    bind(new TypeLiteral[TokenRepository[ConsumedRentToken]] {}).toInstance(consumedRentTokenRepository)

    val tokenRegistry = TokenRegistry(new Random(), reservedRentTokenRepository, consumedRentTokenRepository)
    bind(classOf[TokenRegistry]).toInstance(tokenRegistry)

    bind(classOf[BikeStationRepository]).to(classOf[InMemoryBikeStationRepository]).asEagerSingleton()

    val tripRegistry = TripRegistry(TripCompletionRulesFactory.create)
    bind(classOf[TripRegistry]).toInstance(tripRegistry)

    val bikeShop = new BikeShop()
    bind(classOf[BikeShop]).toInstance(bikeShop)
  }
}