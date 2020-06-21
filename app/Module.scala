import ar.com.flow.bikerental.model.{BikeRepository, InMemoryBikeRepository}
import com.google.inject.AbstractModule
import com.google.inject.name.Names

class Module extends AbstractModule {
  override def configure() = {
    bind(classOf[BikeRepository]).to(classOf[InMemoryBikeRepository]).asEagerSingleton()
  }
}