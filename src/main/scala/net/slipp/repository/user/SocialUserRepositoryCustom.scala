package net.slipp.repository.user

import java.util.List
import net.slipp.domain.user.SocialUser
import org.springframework.util.MultiValueMap

trait SocialUserRepositoryCustom {
  def findsByUserIdAndProviderUserIds(userId: String, providerUserIds: MultiValueMap[String, String]): List[SocialUser]
}

class SocialUserRepositoryImpl extends SocialUserRepositoryCustom {
  def findsByUserIdAndProviderUserIds(userId: String, providerUserIds: MultiValueMap[String, String]): List[SocialUser] = {
    throw new UnsupportedOperationException("findsByUserIdAndProviderUserIds method not supported!")
  }
}
