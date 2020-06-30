package persistence

import anorm._
import ar.com.flow.bikerental.model.{Bike, BikeRepository}
import javax.inject.{Inject, Singleton}
import play.api.db.Database

@Singleton
class AnormBikeRepository @Inject()(db: Database) extends BikeRepository {
  val parser: RowParser[Bike] = Macro.indexedParser[Bike]
  
  override def save(bike: Bike): Unit = {
    db.withConnection { implicit connection =>
      SQL"insert into BIKE (serial_number) values (${bike.serialNumber})".executeInsert()
    }
  }

  override def getBySerialNumber(serialNumber: String): Option[Bike] = {
    val found: Long = db.withConnection { implicit connection =>
      SQL"select count(*) from BIKE where serial_number = $serialNumber".as(SqlParser.scalar[Long].single)
    }
    if (found > 0) Some(Bike(serialNumber)) else None
  }

  override def getAll(): Iterable[Bike] = {
    db.withConnection { implicit connection =>
      SQL"select serial_number from BIKE".as(parser.*)
    }
  }

  override def delete(bike: Bike): Unit = {
    db.withConnection { implicit connection =>
      SQL"delete from BIKE where serial_number=${bike.serialNumber}".executeUpdate()
    }
  }

  override def clear(): Unit = {
    db.withConnection { implicit connection =>
      SQL"delete from BIKE".executeUpdate()
    }
  }
}
