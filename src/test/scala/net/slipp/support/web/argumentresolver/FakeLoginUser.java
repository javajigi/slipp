package net.slipp.support.web.argumentresolver;

import java.lang.annotation.Annotation;

public class FakeLoginUser implements LoginUser {
    private boolean requiredValue = true;

    public FakeLoginUser(boolean requiredValue) {
        this.requiredValue = requiredValue;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return LoginUser.class;
    }

    @Override
    public boolean required() {
        return requiredValue;
    }
}
