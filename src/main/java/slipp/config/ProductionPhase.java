package slipp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ProductionPhase implements Condition {
    private static Logger log = LoggerFactory.getLogger(DevelopmentPhase.class);

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        log.info("current environment : {}", context.getEnvironment().getProperty("environment"));
        return context.getEnvironment().getProperty("environment").equals("PRODUCTION");
    }
}
