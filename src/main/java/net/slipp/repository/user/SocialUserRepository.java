package net.slipp.repository.user;

import java.util.List;
import java.util.Set;

import net.slipp.domain.user.SocialUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface SocialUserRepository extends PagingAndSortingRepository<SocialUser, Long>,
        SocialUserRepositoryCustom {
    @Query("SELECT u FROM SocialUser u WHERE u.userId = :userId ORDER BY u.providerId DESC, u.rank DESC")
    List<SocialUser> findsByUserId(@Param("userId") String userId);

    @Query("SELECT u FROM SocialUser u WHERE u.userId = :userId AND u.providerId = :providerId ORDER BY u.rank DESC")
    List<SocialUser> findsByUserIdAndProviderId(@Param("userId") String userId,
            @Param("providerId") String providerId);

    @Query("SELECT u FROM SocialUser u WHERE u.providerId = :providerId AND u.providerUserId = :providerUserId")
    List<SocialUser> findsByProviderIdAndProviderUserId(
            @Param("providerId") String providerId,
            @Param("providerUserId") String providerUserId);

    @Query("SELECT u FROM SocialUser u WHERE u.userId = :userId AND u.providerId = :providerId")
    SocialUser findByUserIdAndProviderId(@Param("userId") String userId,
            @Param("providerId") String providerId);

    SocialUser findByUserIdAndProviderIdAndProviderUserId(
            @Param("userId") String userId,
            @Param("providerId") String providerId,
            @Param("providerUserId") String providerUserId);

    @Query("SELECT u FROM SocialUser u WHERE u.userId = :userId AND u.providerId = :providerId AND rank = 1")
    List<SocialUser> findsPrimary(@Param("userId") String userId,
            @Param("providerId") String providerId);

    @Query("SELECT max(rank) as rank FROM SocialUser u WHERE u.userId = :userId AND u.providerId = :providerId ")
    List<Integer> findsRank(@Param("userId") String userId,
            @Param("providerId") String providerId);

    @Query("SELECT distinct u.userId FROM SocialUser u where u.providerId = :providerId and u.providerUserId in (:providerUserIds)")
    List<String> findUsersConnectedTo(@Param("providerId") String providerId,
            @Param("providerUserIds") Set<String> providerUserIds);

    SocialUser findByEmail(String email);

    @Query("SELECT u FROM SocialUser u WHERE u.userId LIKE %:searchTerm%")
	Page<SocialUser> findsBySearch(@Param("searchTerm") String searchTerm, Pageable pageable);
}
