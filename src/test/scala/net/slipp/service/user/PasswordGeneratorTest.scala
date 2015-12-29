package net.slipp.service.user

import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.junit.Test

class PasswordGeneratorTest {
  @Test
  @throws(classOf[Exception])
  def randomGenerate {
    val dut: PasswordGenerator = new RandomPasswordGenerator
    val first: String = dut.generate
    val second: String = dut.generate
    assertThat(first == second, is(false))
  }

  @Test
  @throws(classOf[Exception])
  def fixedGenerate_passPassword {
    val password: String = "password"
    val dut: PasswordGenerator = new FixedPasswordGenerator(password)
    assertThat(dut.generate, is(password))
  }

  @Test
  @throws(classOf[Exception])
  def fixedGenerate_null {
    val password: String = "password"
    val dut: PasswordGenerator = new FixedPasswordGenerator
    assertThat(dut.generate, is(password))
  }
}