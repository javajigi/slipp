package net.slipp.support.web

import javax.servlet.{FilterChain, ServletResponse, ServletRequest}

import org.junit.{Before, Test}
import org.springframework.mock.web.{MockFilterChain, MockHttpServletResponse, MockHttpServletRequest}
import org.springframework.web.bind.annotation.RequestMethod

class CorsFilterTest {
  var request: MockHttpServletRequest = _
  var response: MockHttpServletResponse = _
  var chain: FilterChain = _

  @Before def setup(): Unit = {
    request = new MockHttpServletRequest
    response = new MockHttpServletResponse
    chain = new MockFilterChain
  }

  @Test def xFrameOptions_allowFacebook(): Unit = {
    val filter = new CorsFilter
    filter.doFilter(request, response, chain)

    val value = response.getHeader("X-Frame-Options")
    assert(value == "ALLOW-FROM https://apps.facebook.com")
  }

  @Test def xFrameOptions_attachments(): Unit = {
    request.setMethod(RequestMethod.POST.toString)
    request.setRequestURI("/attachments")

    val filter = new CorsFilter
    filter.doFilter(request, response, chain)

    val value = response.getHeader("X-Frame-Options")
    assert(value == "SAMEORIGIN")
  }
}
