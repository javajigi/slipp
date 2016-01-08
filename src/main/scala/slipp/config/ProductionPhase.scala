package slipp.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.`type`.AnnotatedTypeMetadata

class ProductionPhase extends Condition {
  private val log: Logger = LoggerFactory.getLogger(classOf[DevelopmentPhase])

  def matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean = {
    log.info("current environment : {}", context.getEnvironment.getProperty("environment"))
    return context.getEnvironment.getProperty("environment") == "PRODUCTION"
  }
}
