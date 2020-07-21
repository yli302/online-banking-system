package com.green.bank;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.green.bank.database.UserRepository;
import com.green.bank.model.AccountModel;
import com.green.bank.util.DatabaseException;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 2178865317863049636L;

	String UserName, password;
	UserRepository rep = new UserRepository();
	AccountModel am = null;
	boolean pass_wrong = false;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserName = request.getParameter("UserName");
		password = request.getParameter("password");

		System.out.println(UserName);
		System.out.println(password);

		try {
			if (!rep.checkUserExist(UserName, password).isPresent()) {
				request.setAttribute("isPassOK", "No");
				RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
				rd.forward(request, response);
				return;
			} else {
				pass_wrong = true;
				AccountModel am = rep.checkUserExist(UserName, password).get();
				// Setting Session variable for current User
				HttpSession session = request.getSession();
				session.setAttribute("userDetails", am);

				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);

			}
		} catch (DatabaseException | ServletException | IOException e) {
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
	}
}
