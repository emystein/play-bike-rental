import ar.com.flow.bikerental.model.token._
import ar.com.flow.bikerental.model.trip.completion.{TripCompletionRules, TripCompletionRulesFactory}
import ar.com.flow.bikerental.model.{BikeRepository, BikeShop, BikeStationRepository, InMemoryBikeRepository, InMemoryBikeStationRepository, InMemoryUserRepository, TripRegistry, UserRepository}
import com.google.inject.AbstractModule
import persistence.AnormUserRepository

import scala.util.Random

class Module extends AbstractModule {
  override def configure(): Unit = {
//    bind(classOf[UserRepository]).to(classOf[InMemoryUserRepository]).asEagerSingleton()
    bind(classOf[UserRepository]).to(classOf[AnormUserRepository]).asEagerSingleton()

    bind(classOf[BikeRepository]).to(classOf[InMemoryBikeRepository]).asEagerSingleton()

    val tokenRegistry = TokenRegistry(new Random(),
                                      new InMemoryReservedRentTokenRepository(),
                                      new InMemoryConsumedRentTokenRepository
    )
    bind(classOf[TokenRegistry]).toInstance(tokenRegistry)

    bind(classOf[BikeStationRepository]).to(classOf[InMemoryBikeStationRepository]).asEagerSingleton()

    val tripRegistry = TripRegistry(TripCompletionRulesFactory.create)
    bind(classOf[TripRegistry]).toInstance(tripRegistry)

    val bikeShop = new BikeShop()
    bind(classOf[BikeShop]).toInstance(bikeShop)
  }
}