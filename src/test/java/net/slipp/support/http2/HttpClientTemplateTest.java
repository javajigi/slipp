package net.slipp.support.http2;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class HttpClientTemplateTest {
	private HttpClientTemplate dut;
	private Server server;

	@Before
	public void beforeEach() throws Exception {
		int port = 10000 + new Random().nextInt(10000);
		dut = new HttpClientTemplate("http://localhost:" + port);
		server = new Server(port);
		Context root = new Context(server, "/", Context.SESSIONS);
		root.addServlet(new ServletHolder(new EchoServlet()), "/*");
		server.start();
	}

	@After
	public void afterEach() throws Exception {
		server.stop();
	}

	@SuppressWarnings("serial")
	static class EchoServlet extends HttpServlet {
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			resp.getWriter().print(req.getParameter("message"));
		}

		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			doGet(req, resp);
		}
	}

	static class SimpleInvocation extends HttpInvocationSupport {
		protected SimpleInvocation(String message) {
			super("/");
			addParameter("message", message);
		}

		protected Object parseResponseBody(String body) throws Exception {
			return body;
		}
	}

	@Test
	public void successfulGetRequest() {
		assertEquals("hello", dut.get(new SimpleInvocation("hello")));
	}

	@Test
	public void successfulPostRequest() {
		assertEquals("hello", dut.post(new SimpleInvocation("hello")));
	}
}
