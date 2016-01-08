package net.slipp.repository.user

import java.lang.Long
import java.util.List
import java.util.Set
import net.slipp.domain.user.SocialUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param

trait SocialUserRepository extends PagingAndSortingRepository[SocialUser, Long] with SocialUserRepositoryCustom {
  @Query("SELECT u FROM SocialUser u WHERE u.userId = :userId ORDER BY u.providerId DESC, u.rank DESC")
  def findsByUserId(@Param("userId") userId: String): List[SocialUser]

  @Query("SELECT u FROM SocialUser u WHERE u.userId = :userId AND u.providerId = :providerId ORDER BY u.rank DESC")
  def findsByUserIdAndProviderId(@Param("userId") userId: String, @Param("providerId") providerId: String): List[SocialUser]

  @Query("SELECT u FROM SocialUser u WHERE u.providerId = :providerId AND u.providerUserId = :providerUserId")
  def findsByProviderIdAndProviderUserId(@Param("providerId") providerId: String, @Param("providerUserId") providerUserId: String): List[SocialUser]

  @Query("SELECT u FROM SocialUser u WHERE u.userId = :userId AND u.providerId = :providerId")
  def findByUserIdAndProviderId(@Param("userId") userId: String, @Param("providerId") providerId: String): SocialUser

  def findByUserIdAndProviderIdAndProviderUserId(@Param("userId") userId: String, @Param("providerId") providerId: String, @Param("providerUserId") providerUserId: String): SocialUser

  @Query("SELECT u FROM SocialUser u WHERE u.userId = :userId AND u.providerId = :providerId AND rank = 1")
  def findsPrimary(@Param("userId") userId: String, @Param("providerId") providerId: String): List[SocialUser]

  @Query("SELECT max(rank) as rank FROM SocialUser u WHERE u.userId = :userId AND u.providerId = :providerId ")
  def findsRank(@Param("userId") userId: String, @Param("providerId") providerId: String): List[Integer]

  @Query("SELECT distinct u.userId FROM SocialUser u where u.providerId = :providerId and u.providerUserId in (:providerUserIds)")
  def findUsersConnectedTo(@Param("providerId") providerId: String, @Param("providerUserIds") providerUserIds: Set[String]): List[String]

  def findByEmail(email: String): SocialUser

  @Query("SELECT u FROM SocialUser u WHERE u.userId LIKE %:searchTerm%")
  def findsBySearch(@Param("searchTerm") searchTerm: String, pageable: Pageable): Page[SocialUser]
}
