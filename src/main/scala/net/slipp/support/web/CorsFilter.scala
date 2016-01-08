package net.slipp.support.web

import javax.servlet._
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

class CorsFilter extends Filter {
  def init(filterConfig: FilterConfig) {
  }

  def doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
    val request = servletRequest.asInstanceOf[HttpServletRequest]
    val response = servletResponse.asInstanceOf[HttpServletResponse]
    val uri = request.getRequestURI

    uri match {
      case "/attachments" => response.setHeader("X-Frame-Options", "SAMEORIGIN")
      case _ => response.setHeader("X-Frame-Options", "ALLOW-FROM https://apps.facebook.com")
    }

    filterChain.doFilter(servletRequest, servletResponse)
  }

  def destroy {
  }
}
