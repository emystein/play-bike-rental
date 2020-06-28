package persistence

import ar.com.flow.bikerental.model.{Bike, BikeRepository}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest

class AnormBikeRepositorySpec extends PlaySpec with GuiceOneAppPerTest {
  "AnormBikeRepository" should {
    "create and retrieve bike" in {
      val repository = app.injector.instanceOf[BikeRepository]

      repository.save(Bike("1"))

      val retrievedBike = repository.getBySerialNumber("1")

      retrievedBike mustBe Some(Bike("1"))
    }
    "delete bike" in {
      val repository = app.injector.instanceOf[BikeRepository]

      val bike = Bike("1")
      repository.save(bike)

      repository.delete(bike)

      repository.getBySerialNumber("1") mustBe None
    }
    "get all bikes" in {
      val repository = app.injector.instanceOf[BikeRepository]

      val allBikes = List(Bike("1"), Bike("2"))

      allBikes.foreach(bike => repository.save(bike))

      repository.getAll() must contain theSameElementsAs allBikes
    }
  }
}
