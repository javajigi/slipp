package net.slipp.domain.user

class ExistedUserException(arg0: String, arg1: Throwable) extends Exception(arg0, arg1) {
  def this() = this(null, null)

  def this(arg0: String) = this(arg0, null)

  def this(arg0: Throwable) = this(null, arg0)
}
