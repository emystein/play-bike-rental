package persistence

import java.util.UUID

import ar.com.flow.bikerental.model.{User, UserRepository}
import javax.inject.{Inject, Singleton}
import play.api.db.Database
import anorm._

@Singleton
class JdbcUserRepository @Inject()(db: Database) extends UserRepository {
  override def save(user: User): User = {
    val savedUser = User(Some(user.id.getOrElse(UUID.randomUUID().toString)), user.name)

    db.withConnection { implicit connection =>
      SQL(s"insert into USER (id, name) values ({id}, {name})")
        .on("id" -> savedUser.id.get, "name" -> savedUser.name)
        .executeInsert()
    }

    savedUser
  }

  override def getById(userId: String): Option[User] = {
    val name = db.withConnection { implicit connection =>
      SQL(s"select name from USER where ID={id}")
        .on("id" -> userId)
        .as(SqlParser.str("name").singleOpt)
    }

    name.map(User(Some(userId), _))
  }
}