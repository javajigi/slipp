<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
</head>
<body>

<p>Hello World : 

<c:choose>
<c:when test="${securityLevel eq 'Protected'}" >
Protected Area.
</c:when>
<c:when test="${securityLevel eq 'Public'}" >

Public Area. 
<p> <a href="/protected">Attempt to access</a> a protected resource</p>
</c:when>
</c:choose>
</p>

                    <authz:authorize access="!hasRole('ROLE_USER')">
						<p>You are not logged in. &nbsp;<a href="/oauthlogin.jsp" />Login</a></p>
                    </authz:authorize>
                                  <authz:authorize access="hasRole('ROLE_USER')">
						You are logged in locally as <c:out value="${userName}" />. &nbsp;<a href="/logout">Logout</a></p>
						
                    </authz:authorize>
                      
                      <authz:authorize access="hasRole('ROLE_USER_TWITTER')">
					 <p>	You are connected with Twitter. </p>
                    </authz:authorize>
                      <authz:authorize access="hasRole('ROLE_USER_FACEBOOK')">
						<p>You are connected with Facebook. </p>
                    </authz:authorize>

           
                    <authz:authorize access="hasRole('ROLE_USER') and !hasRole('ROLE_USER_FACEBOOK')">
						<p><a href="/oauthconnect.jsp">Connect</a> your account with Facebook</p>
                    </authz:authorize>
                    <authz:authorize access="hasRole('ROLE_USER') and !hasRole('ROLE_USER_TWITTER')">
						<p><a href="/oauthconnect.jsp">Connect</a> your account with Twitter</p>
                    </authz:authorize>
                       
                 
</body>                    
</html>