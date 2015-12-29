package net.slipp.support.web

import javax.servlet._
import javax.servlet.http.HttpServletResponse

class CorsFilter extends Filter {
  def init(filterConfig: FilterConfig) {
  }

  def doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
    val response: HttpServletResponse = servletResponse.asInstanceOf[HttpServletResponse]
    response.setHeader("X-Frame-Options", "ALLOW-FROM https://apps.facebook.com")
    filterChain.doFilter(servletRequest, servletResponse)
  }

  def destroy {
  }
}
