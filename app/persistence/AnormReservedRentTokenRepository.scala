package persistence

import anorm._
import ar.com.flow.bikerental.model.{Bike, User}
import ar.com.flow.bikerental.model.token.{ReservedRentToken, TokenRegistry, TokenRepository}
import javax.inject.{Inject, Singleton}
import play.api.db.Database
import rest.controllers.ReservedRentTokenDto

@Singleton
class AnormReservedRentTokenRepository  @Inject()(db: Database) extends TokenRepository[ReservedRentToken] {
  implicit val userParser = Macro.parser[User]("owner_id", "name")
  val parser: RowParser[ReservedRentTokenDto] = Macro.indexedParser[ReservedRentTokenDto]

  override def save(token: ReservedRentToken): Unit = {
    db.withConnection { implicit connection =>
      SQL"""insert into RESERVED_RENT_TOKEN (id, owner_id, expiration)
            values (${token.value}, ${token.owner.id}, ${token.expiration})"""
            .executeUpdate()
    }
  }

  override def getById(id: String): Option[ReservedRentToken] = {
    val retrievedDto: Option[ReservedRentTokenDto] = db.withConnection { implicit connection =>
      SQL"""select token.id, token.expiration, token.owner_id, user.name
            from RESERVED_RENT_TOKEN token, USER user
            where token.id = $id
            and token.owner_id = user.id"""
        .as(parser.singleOpt)
    }

   // TODO inject TokenRegistry to ReservedRentToken
   retrievedDto.map(d => ReservedRentToken(d.value, d.expiration, d.owner))
  }

  override def getAll(): Iterable[ReservedRentToken] = {
    val retrievedDtos = db.withConnection { implicit connection =>
      SQL"""select token.id, token.expiration, token.owner_id, user.name
            from RESERVED_RENT_TOKEN token, USER user
            where token.owner_id = user.id"""
        .as(parser.*)
    }

    retrievedDtos.map(d => ReservedRentToken(d.value, d.expiration, d.owner))
  }

  override def getAllByUser(user: User): Iterable[ReservedRentToken] = {
    val retrievedDtos = db.withConnection { implicit connection =>
      SQL"""select token.id, token.expiration, token.owner_id, user.name
            from RESERVED_RENT_TOKEN token, USER user
            where token.owner_id = user.id
            and token.owner_id = ${user.id}"""
        .as(parser.*)
    }

    retrievedDtos.map(d => ReservedRentToken(d.value, d.expiration, d.owner))
  }

  override def contains(token: ReservedRentToken): Boolean = {
    // TODO re-implement with SQL query
    getAll().exists(_.value == token.value)
  }

  override def clear(): Unit = {
    db.withConnection { implicit connection =>
      SQL"delete from RESERVED_RENT_TOKEN".executeUpdate()
    }
  }
}
