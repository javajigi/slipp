package net.slipp.repository.user

import java.lang.Long
import java.util.ArrayList
import java.util.List
import net.slipp.domain.user.SocialUser
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionData
import org.springframework.social.connect.ConnectionFactory
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.ConnectionKey
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.connect.DuplicateConnectionException
import org.springframework.social.connect.NoSuchConnectionException
import org.springframework.social.connect.NotConnectedException
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import scala.collection.JavaConversions._

class JpaConnectionRepository(
                             userId: String,
                             socialUserRepository: SocialUserRepository,
                             connectionFactoryLocator: ConnectionFactoryLocator,
                             textEncryptor: TextEncryptor) extends ConnectionRepository {
  def findAllConnections: MultiValueMap[String, Connection[_]] = {
    val resultList: List[Connection[_]] = connectionMapper.mapEntities(socialUserRepository.findsByUserId(userId))
    val connections: MultiValueMap[String, Connection[_]] = new LinkedMultiValueMap[String, Connection[_]]
    for (connection <- resultList) {
      val providerId: String = connection.getKey.getProviderId
      connections.add(providerId, connection)
    }
    return connections
  }

  def findConnections(providerId: String): List[Connection[_]] = {
    return connectionMapper.mapEntities(socialUserRepository.findsByUserIdAndProviderId(userId, providerId))
  }

  @SuppressWarnings(Array("unchecked")) def findConnections[A](apiType: Class[A]): List[Connection[A]] = {
    val connections: List[_] = findConnections(getProviderId(apiType))
    return connections.asInstanceOf[List[Connection[A]]]
  }

  def findConnectionsToUsers(providerUserIds: MultiValueMap[String, String]): MultiValueMap[String, Connection[_]] = {
    if (providerUserIds.isEmpty) {
      throw new IllegalArgumentException("Unable to execute find: no providerUsers provided")
    }
    val resultList: List[Connection[_]] = connectionMapper.mapEntities(socialUserRepository.findsByUserIdAndProviderUserIds(userId, providerUserIds))
    val connectionsForUsers: MultiValueMap[String, Connection[_]] = new LinkedMultiValueMap[String, Connection[_]]
    import scala.collection.JavaConversions._
    for (connection <- resultList) {
      val providerId: String = connection.getKey.getProviderId
      val userIds: List[String] = providerUserIds.get(providerId)
      var connections: List[Connection[_]] = connectionsForUsers.get(providerId)
      if (connections == null) {
        connections = new ArrayList[Connection[_]](userIds.size)
        {
          var i: Int = 0
          while (i < userIds.size) {
            {
              connections.add(null)
            }
            ({
              i += 1; i - 1
            })
          }
        }
        connectionsForUsers.put(providerId, connections)
      }
      val providerUserId: String = connection.getKey.getProviderUserId
      val connectionIndex: Int = userIds.indexOf(providerUserId)
      connections.set(connectionIndex, connection)
    }
    return connectionsForUsers
  }

  def getConnection(connectionKey: ConnectionKey): Connection[_] = {
    try {
      return connectionMapper.mapEntity(socialUserRepository.findByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId, connectionKey.getProviderUserId))
    }
    catch {
      case e: EmptyResultDataAccessException => {
        throw new NoSuchConnectionException(connectionKey)
      }
    }
  }

  @SuppressWarnings(Array("unchecked")) def getConnection[A](apiType: Class[A], providerUserId: String): Connection[A] = {
    val providerId: String = getProviderId(apiType)
    return getConnection(new ConnectionKey(providerId, providerUserId)).asInstanceOf[Connection[A]]
  }

  @SuppressWarnings(Array("unchecked")) def getPrimaryConnection[A](apiType: Class[A]): Connection[A] = {
    val providerId: String = getProviderId(apiType)
    val connection: Connection[A] = findPrimaryConnection(providerId).asInstanceOf[Connection[A]]
    if (connection == null) {
      throw new NotConnectedException(providerId)
    }
    return connection
  }

  @SuppressWarnings(Array("unchecked")) def findPrimaryConnection[A](apiType: Class[A]): Connection[A] = {
    val providerId: String = getProviderId(apiType)
    return findPrimaryConnection(providerId).asInstanceOf[Connection[A]]
  }

  private def findPrimaryConnection(providerId: String): Connection[_] = {
    val connections: List[Connection[_]] = connectionMapper.mapEntities(socialUserRepository.findsPrimary(userId, providerId))
    if (connections.size > 0) {
      return connections.get(0)
    }
    else {
      return null
    }
  }

  def addConnection(connection: Connection[_]) {
    try {
      val data: ConnectionData = connection.createData
      val rank: Int = getRank(data.getProviderId)
      val socialUser: SocialUser = new SocialUser
      socialUser.setUserId(userId)
      socialUser.setProviderId(data.getProviderId)
      socialUser.setProviderUserId(data.getProviderUserId)
      socialUser.setRank(rank)
      socialUser.setDisplayName(data.getDisplayName)
      socialUser.setProfileUrl(data.getProfileUrl)
      socialUser.setImageUrl(data.getImageUrl)
      socialUser.setAccessToken(encrypt(data.getAccessToken))
      socialUser.setSecret(encrypt(data.getSecret))
      socialUser.setRefreshToken(encrypt(data.getRefreshToken))
      socialUser.setExpireTime(data.getExpireTime)
      socialUserRepository.save(socialUser)
    }
    catch {
      case e: DuplicateKeyException => {
        throw new DuplicateConnectionException(connection.getKey)
      }
    }
  }

  private def getRank(providerId: String): Int = {
    val result: List[Integer] = socialUserRepository.findsRank(userId, providerId)
    if (result.isEmpty || result.get(0) == null) return 1
    return result.get(0) + 1
  }

  def updateConnection(connection: Connection[_]) {
    val data: ConnectionData = connection.createData
    val su: SocialUser = socialUserRepository.findByUserIdAndProviderIdAndProviderUserId(userId, data.getProviderId, data.getProviderUserId)
    if (su != null) {
      su.setDisplayName(data.getDisplayName)
      su.setProfileUrl(data.getProfileUrl)
      su.setImageUrl(data.getImageUrl)
      su.setAccessToken(encrypt(data.getAccessToken))
      su.setSecret(encrypt(data.getSecret))
      su.setRefreshToken(encrypt(data.getRefreshToken))
      su.setExpireTime(data.getExpireTime)
      socialUserRepository.save(su)
    }
  }

  def removeConnections(providerId: String) {
    val socialUser: SocialUser = socialUserRepository.findByUserIdAndProviderId(userId, providerId)
    socialUserRepository.delete(socialUser)
  }

  def removeConnection(connectionKey: ConnectionKey) {
    val socialUser: SocialUser = socialUserRepository.findByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId, connectionKey.getProviderUserId)
    socialUserRepository.delete(socialUser)
  }

  private final val connectionMapper: ServiceProviderConnectionMapper = new ServiceProviderConnectionMapper

  class ServiceProviderConnectionMapper {
    def mapEntities(socialUsers: List[SocialUser]): List[Connection[_]] = {
      val result: List[Connection[_]] = new ArrayList[Connection[_]]
      for (su <- socialUsers) {
        result.add(mapEntity(su))
      }
      return result
    }

    def mapEntity(socialUser: SocialUser): Connection[_] = {
      val connectionData: ConnectionData = mapConnectionData(socialUser)
      val connectionFactory: ConnectionFactory[_] = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId)
      return connectionFactory.createConnection(connectionData)
    }

    private def mapConnectionData(socialUser: SocialUser): ConnectionData = {
      return new ConnectionData(socialUser.getProviderId, socialUser.getProviderUserId, socialUser.getDisplayName, socialUser.getProfileUrl, socialUser.getImageUrl, decrypt(socialUser.getAccessToken), decrypt(socialUser.getSecret), decrypt(socialUser.getRefreshToken), expireTime(socialUser.getExpireTime))
    }

    private def decrypt(encryptedText: String): String = {
      return if (encryptedText != null) textEncryptor.decrypt(encryptedText) else encryptedText
    }

    private def expireTime(expireTime: Long): Long = {
      Option(expireTime) match {
        case Some(ex) => ex
        case None => 0L
      }
    }
  }

  private def getProviderId[A](apiType: Class[A]): String = {
    return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId
  }

  private def encrypt(text: String): String = {
    return if (text != null) textEncryptor.encrypt(text) else text
  }
}
