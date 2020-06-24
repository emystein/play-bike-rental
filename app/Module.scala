import ar.com.flow.bikerental.model.token._
import ar.com.flow.bikerental.model.{BikeRepository, BikeStationRepository, InMemoryBikeRepository, InMemoryBikeStationRepository, InMemoryUserRepository, UserRepository}
import com.google.inject.AbstractModule

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[UserRepository]).to(classOf[InMemoryUserRepository]).asEagerSingleton()

    bind(classOf[BikeRepository]).to(classOf[InMemoryBikeRepository]).asEagerSingleton()

    val tokenRegistry = TokenRegistry(new TokenGenerator(),
                                      new InMemoryRentTokenRepository(),
                                      new InMemoryConsumedRentTokenRepository
    )
    bind(classOf[TokenRegistry]).toInstance(tokenRegistry)

    bind(classOf[BikeStationRepository]).to(classOf[InMemoryBikeStationRepository]).asEagerSingleton()
  }
}