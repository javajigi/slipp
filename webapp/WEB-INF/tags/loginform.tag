<%@ tag language="java" pageEncoding="UTF-8"%><%@ tag body-content="empty" %><%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@
taglib prefix="form" uri="http://www.springframework.org/tags/form"%><%@
attribute name="redirectUrl" required="true" rtexprvalue="true" type="java.lang.String" description="" %>
	
	        <div class="choose-login-type">
	            <div class="login-with-slipp">
	                <p class="login-with-slipp-text">SLiPP 계정으로 로그인하세요.</p>
	                <form id="authentication" action="/users/authenticate" method="POST">
	                	<input type="hidden" name="redirect" value="${redirectUrl}" />
	                    <input id="authenticationId" name="authenticationId" class="inp-email" placeholder="이메일" type="text" value=""/>
	                    <input id="authenticationPassword"  name="authenticationPassword" class="inp-title" placeholder="비밀번호" type="password" value=""/>
	                    <c:if test="${not empty param.login_error}">
	                    <label class="error" style="">메일주소 혹은 비밀번호가 정확하지 않습니다.</label>
	                    </c:if>
	                    <p class="login-with-slipp-submit">
	                        <label><input name="_spring_security_remember_me" class="inp-pw" type="checkbox" value="true"/> 자동로그인</label>
	                        <button id="loginSubmitBtn" type="submit" class="login-with-slipp-submit-btn"><i class="icon-login"></i> 로그인</button>
	                    </p>
	                </form>
	            </div>
	            <div class="divide-bar left"></div>
	            <div class="login-with-sns">
	                <p class="login-with-sns-text">또는, SNS 계정으로 로그인하세요.</p>
	                <form action="/signin/facebook" method="POST">
	                    <input type="hidden" name="scope" value="publish_stream,user_groups" />
	                    <button type="submit" class="btn-login-facebook"><i class="foundicon-facebook"></i> 페이스북</button>
	                </form>
	                <form action="/signin/twitter" method="POST">
	                    <button type="submit" class="btn-login-twitter"><i class="foundicon-twitter"></i> 트위터</button>
	                </form>
	                <form action="/signin/google?scope=https://www.googleapis.com/auth/userinfo.profile" method="POST">
	                    <button type="submit" class="btn-login-google"><i class="foundicon-google-plus"></i> 구글</button>
	                </form>
	            </div>
	            <div class="divide-bar right"></div>
	            <div class="sign-in-to-slipp">
	                <p class="sign-in-to-slipp-text">계정이 없다면 간단히 만들어보세요.</p>
	                <form:form modelAttribute="user" cssClass="form-write" action="/users" method="post">
	                		<input type="hidden" name="redirect" value="${redirectUrl}" />
	                        <form:input path="email" class="inp_email" placeholder="이메일" />
	                        <form:input path="userId" class="inp_pw" placeholder="닉네임" />
	                        <p class="sign-in-to-slipp-notice">
	                            - 등록한 메일로 임시 비밀번호를 보내드립니다. <br />
	                            - 개인공간에서 비밀번호를 변경할 수 있습니다.</p>
	                        <button type="submit" class="sign-in-to-slipp-btn"><i class="icon-signin"></i> 회원가입</button>
	                </form:form>
	            </div>
	        </div>