package persistence

import java.util.UUID

import ar.com.flow.bikerental.model.{User, UserRepository}
import javax.inject.{Inject, Singleton}
import play.api.db.Database
import anorm._

@Singleton
class AnormUserRepository @Inject()(db: Database) extends UserRepository {
  override def save(user: User): User = {
    val id = db.withConnection { implicit connection =>
      SQL"insert into USER (name) values (${user.name})".executeInsert()
    }

    User(id, user.name)
  }

  override def getById(userId: Long): Option[User] = {
    val name = db.withConnection { implicit connection =>
      SQL"select name from USER where id = $userId".as(SqlParser.str("name").singleOpt)
    }

    name.map(User(Some(userId), _))
  }
}