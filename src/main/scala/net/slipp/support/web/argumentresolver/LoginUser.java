package net.slipp.support.web.argumentresolver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.slipp.support.security.LoginRequiredException;

/**
 * 로그인 사용자 웹 객체를 컨트롤러의 인자로 받게 해준다.
 *
 * {@link LoginUserHandlerMethodArgumentResolver}가 사용한다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {
	/**
	 * 로그인이 필수인지 여부를 정한다.
	 * true이면 비로그인 사용자 접근시 예외가 발생한다. {@link LoginRequiredException}
	 * @return
	 */
	boolean required() default true;
}
