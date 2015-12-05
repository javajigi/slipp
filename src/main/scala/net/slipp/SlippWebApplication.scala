package net.slipp

import org.springframework.boot.SpringApplication
import slipp.config.ApplicationConfig

object SlippWebApplication extends App {
  SpringApplication.run(classOf[ApplicationConfig])
}