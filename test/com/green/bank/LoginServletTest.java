package com.green.bank;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.green.bank.database.UserRepository;
import com.green.bank.model.AccountModel;

@RunWith(MockitoJUnitRunner.class)
public class LoginServletTest {
	LoginServlet svl = new LoginServlet();
	@Mock
	HttpServletRequest req;
	@Mock
	HttpServletResponse resp;
	@Mock
	RequestDispatcher rd;
	@Mock
	UserRepository rep;
	@Mock
	AccountModel am = new AccountModel();

	@Test
	public void negativeTestPostNullRequestParameters() throws ServletException, IOException {
		Mockito.when(req.getRequestDispatcher("login.jsp")).thenReturn(rd);
		Mockito.when(req.getRequestDispatcher("index.jsp")).thenReturn(rd);
		svl.doPost(req, resp);
		Mockito.verify(req, Mockito.times(2)).getParameter(Mockito.anyString());
		Mockito.verify(req, Mockito.times(1)).setAttribute(Mockito.anyString(), Mockito.anyObject());
		Mockito.verify(rd, Mockito.times(1)).forward(req, resp);
	}

	@Test
	public void negativeTestPostEmptyRequestParameters() throws ServletException, IOException {
		Mockito.when(req.getRequestDispatcher("login.jsp")).thenReturn(rd);
		Mockito.when(req.getRequestDispatcher("index.jsp")).thenReturn(rd);
		Mockito.when(req.getParameter(Mockito.anyString())).thenReturn("");
		svl.doPost(req, resp);
		Mockito.verify(req, Mockito.times(2)).getParameter(Mockito.anyString());
		Mockito.verify(req, Mockito.times(1)).setAttribute(Mockito.anyString(), Mockito.anyObject());
		Mockito.verify(rd, Mockito.times(1)).forward(req, resp);
	}

	@Test
	public void positiveTestPostUserExistInDatabase() throws ServletException, IOException {
		Mockito.when(req.getRequestDispatcher("login.jsp")).thenReturn(rd);
		Mockito.when(req.getRequestDispatcher("index.jsp")).thenReturn(rd);
		Mockito.when(req.getParameter(Mockito.anyString())).thenReturn("");
		Mockito.when(rep.checkUserExist(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(am));
		svl.doPost(req, resp);
		Mockito.verify(req, Mockito.times(2)).getParameter(Mockito.anyString());
		Mockito.verify(req, Mockito.times(1)).setAttribute(Mockito.anyString(), Mockito.anyObject());
		Mockito.verify(rd, Mockito.times(1)).forward(req, resp);
	}
}
