package net.slipp.social.security

import java.util.Collection
import net.slipp.domain.ProviderType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class SlippUser(username: String, password: String, providerType: ProviderType, authorities: Collection[_ <: GrantedAuthority])
  extends User(username, password, authorities) {

  def getProviderType: ProviderType = {
    providerType
  }
}
