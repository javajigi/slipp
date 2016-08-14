package net.slipp

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import
import slipp.config.ApplicationConfig

object SlippApplication {
  @throws(classOf[Exception])
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[SlippApplication])
  }
}

@SpringBootApplication
@Import(value = Array(classOf[ApplicationConfig]))
class SlippApplication {

}
