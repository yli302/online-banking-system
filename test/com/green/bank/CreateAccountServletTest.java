package com.green.bank;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.green.bank.model.AccountModel;

// mock object:
@RunWith(MockitoJUnitRunner.class)
public class CreateAccountServletTest {
	CreateAccountServlet svl = new CreateAccountServlet();
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletRequest request1;
	@Mock
	HttpServletResponse response;
	@Mock
	RequestDispatcher rd;

	@Test
	@Ignore
	public void NegativeTestPostNullRequestParameters() throws ServletException, IOException {
		Mockito.when(request.getRequestDispatcher("create_account.jsp")).thenReturn(rd);
		svl.doPost(request, response);
		Mockito.verify(rd, Mockito.times(1)).forward(request, response);
		assertNull(request.getAttribute("Account_details"));
	}

	@Test
	@Ignore
	public void negativeTestPostEmptyRequestParameters() throws ServletException, IOException {
		Mockito.when(request.getParameter(Mockito.anyString())).thenReturn("");
		Mockito.when(request.getRequestDispatcher("create_account.jsp")).thenReturn(rd);
		svl.doPost(request, response);
		Mockito.verify(rd, Mockito.times(1)).forward(request, response);
		Mockito.verify(request).setAttribute("error", Mockito.anyObject());
		assertNull(request.getAttribute("Account_details"));
		assertNotNull(request.getAttribute("error"));
	}

	@Test
	@Ignore
	public void positiveTestPost() throws ServletException, IOException {
		Mockito.when(request.getParameter("password")).thenReturn("abc%0HGDD");
		Mockito.when(request.getParameter("re_password")).thenReturn("abc%0HGDD");
		Mockito.when(request.getParameter(Mockito.anyString())).thenReturn("123456789");
		Mockito.when(request.getRequestDispatcher("create_account.jsp")).thenReturn(rd);
		svl.doPost(request, response);
		Mockito.verify(rd, Mockito.times(1)).forward(request, response);
		Mockito.verify(request).setAttribute(Mockito.anyString(), Mockito.anyObject());
	}

	@Test
	public void testMultipleThreadsNotImpacted() throws ServletException, IOException, InterruptedException {
		RequestWrapper request1 = new RequestWrapper(request);
		Mockito.when(request.getParameter(Mockito.anyString())).thenReturn("1545432376");
		Mockito.when(request.getParameter("password")).thenReturn("abc%0HGDD");
		Mockito.when(request.getParameter("re_password")).thenReturn("abc%0HGDD");
		Mockito.when(request.getRequestDispatcher("create_account_progress.jsp")).thenReturn(rd);

		RequestWrapper request2 = new RequestWrapper(request1);
		Mockito.when(request1.getParameter(Mockito.anyString())).thenReturn("1345678901");
		Mockito.when(request1.getParameter("password")).thenReturn("dbc%0HGDD");
		Mockito.when(request1.getParameter("re_password")).thenReturn("dbc%0HGDD");
		Mockito.when(request1.getRequestDispatcher("create_account_progress.jsp")).thenReturn(rd);
		Thread t1 = new Thread(() -> {
			try {
				svl.doPost(request2, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		});
		Thread t2 = new Thread(() -> {
			try {
				svl.doPost(request2, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		});
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		AccountModel o1 = (AccountModel) request1.getAttribute("Account_details");

		AccountModel o2 = (AccountModel) request2.getAttribute("Account_details");
		Assert.assertNotEquals(o1, o2);
		Assert.assertEquals(o1.getPassword(), "abc%0HGDD");
		Assert.assertEquals(o2.getPassword(), "dbc%0HGDD");
		Assert.assertEquals(o1.getAddress(), "1545432376");
		Assert.assertEquals(o2.getAddress(), "1345678901");
	}
}

class RequestWrapper extends HttpServletRequestWrapper {
	public RequestWrapper(HttpServletRequest request) {
		super(request);
	}

	Map<String, Object> map = new HashMap<>();

	@Override
	public void setAttribute(String name, Object o) {
		map.put(name, o);
	}

	@Override
	public Object getAttribute(String name) {
		return map.get(name);
	}
}