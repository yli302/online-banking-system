package com.green.bank;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginServletIntegrationTest {

	LoginServlet servlet = new LoginServlet();
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	RequestDispatcher rd;

	@Test
	public void testUserNotPresent() throws ServletException, IOException {
		when(request.getRequestDispatcher("login.jsp")).thenReturn(rd);
		when(request.getParameter("UserName")).thenReturn("111111");
		when(request.getParameter("password")).thenReturn("111111");// special, number character, 6
		servlet.doPost(request, response);
		Mockito.verify(rd, times(1)).forward(request, response);
		Mockito.verify(request).setAttribute(anyString(), anyString());
	}
}