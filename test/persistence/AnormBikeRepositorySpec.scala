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

      val deletedBike = repository.getBySerialNumber("1")
      deletedBike mustBe None
    }
    "get all bikes" in {
      val repository = app.injector.instanceOf[BikeRepository]

      val bike1 = Bike("1")
      repository.save(bike1)
      val bike2 = Bike("2")
      repository.save(bike2)

      val allBikes = repository.getAll()

      allBikes must contain theSameElementsAs allBikes
    }
  }
}
