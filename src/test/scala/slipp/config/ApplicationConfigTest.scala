package slipp.config

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[ApplicationConfig]))
class ApplicationConfigTest {
  @Test def load {
  }
}