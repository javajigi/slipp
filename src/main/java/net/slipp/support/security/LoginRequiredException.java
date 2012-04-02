package net.slipp.support.security;

import org.springframework.core.ErrorCoded;

public class LoginRequiredException extends Exception implements ErrorCoded {
    private static final long serialVersionUID = 7598014391903123780L;

    private static final String ERROR_CODE = "exception.login.required";
    
    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}
