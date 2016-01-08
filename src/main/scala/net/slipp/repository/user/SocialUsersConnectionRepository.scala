package net.slipp.repository.user

import java.util.{ArrayList, HashSet, List, Set}

import net.slipp.domain.user.SocialUser
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.social.connect.{Connection, ConnectionFactoryLocator, ConnectionKey, ConnectionRepository, ConnectionSignUp, UsersConnectionRepository}

class SocialUsersConnectionRepository(socialUserRepository: SocialUserRepository,
                                     connectionFactoryLocator: ConnectionFactoryLocator,
                                     textEncryptor: TextEncryptor) extends UsersConnectionRepository {
  private var connectionSignUp: ConnectionSignUp = null

  def setConnectionSignUp(connectionSignUp: ConnectionSignUp) {
    this.connectionSignUp = connectionSignUp
  }

  def findUserIdsWithConnection(connection: Connection[_]): List[String] = {
    val usrs: List[String] = new ArrayList[String]
    val key: ConnectionKey = connection.getKey
    val users: List[SocialUser] = socialUserRepository.findsByProviderIdAndProviderUserId(key.getProviderId, key.getProviderUserId)
    if (!users.isEmpty) {
      import scala.collection.JavaConversions._
      for (user <- users) {
        usrs.add(user.getUserId)
      }
      return usrs
    }
    if (connectionSignUp != null) {
      val newUserId: String = connectionSignUp.execute(connection)
      if (newUserId == null) return usrs
      createConnectionRepository(newUserId).addConnection(connection)
      usrs.add(newUserId)
    }
    return usrs
  }

  def findUserIdsConnectedTo(providerId: String, providerUserIds: Set[String]): Set[String] = {
    val userIds: List[String] = socialUserRepository.findUsersConnectedTo(providerId, providerUserIds)
    return new HashSet[String](userIds)
  }

  def createConnectionRepository(userId: String): ConnectionRepository = {
    if (userId == null) {
      throw new IllegalArgumentException("userId cannot be null")
    }
    return new JpaConnectionRepository(userId, socialUserRepository, connectionFactoryLocator, textEncryptor)
  }
}
