package slipp.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource(Array("classpath:application-properties.xml"))
class TestConfig {
}
