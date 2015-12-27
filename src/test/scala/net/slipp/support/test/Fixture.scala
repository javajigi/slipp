package net.slipp.support.test

import net.slipp.domain.user.SocialUser

trait Fixture {
  implicit def aSomeUser(id: Long = 1L, userId: String = "someUserId", email: String = "some@sample.com") = {
    val user = new SocialUser()
    user.setId(id)
    user.setUserId(userId)
    user.setEmail(email)
    user
  }
}
