package com.green.bank;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.green.bank.database.JDBC_Connect;
import com.green.bank.model.AccountModel;
import com.green.bank.util.DatabaseException;

public class TransferServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String account_no, username, target_acc_no, password;
		boolean pass_wrong = false;
		int own_amount = 0, transfer_amount, recipient_amount = 0;
		ReadWriteLock userLock = new ReentrantReadWriteLock();
		ReadWriteLock adminLock = new ReentrantReadWriteLock();
		Lock userWriteLock = userLock.writeLock();
		Lock userReadLock = userLock.readLock();
		Lock adminWriteLock = adminLock.writeLock();
		Lock adminReadLock = adminLock.readLock();

		account_no = request.getParameter("account_no");
		username = request.getParameter("username");
		target_acc_no = request.getParameter("target_acc_no");
		password = request.getParameter("password");
		transfer_amount = Integer.parseInt(request.getParameter("amount"));

		try {
			Connection conn = JDBC_Connect.getConnection();

			Statement stmt = conn.createStatement();

			Connection conn1 = JDBC_Connect.getConnection();

			Statement stmt1 = conn1.createStatement();

			ResultSet rsOwn = stmt.executeQuery("select * from account where id='" + account_no + "' and username='"
					+ username + "' and password='" + password + "'");

			ResultSet rstTarget = stmt1.executeQuery("select * from account where id='" + target_acc_no + "'");

			if (!rsOwn.isBeforeFirst() && !rstTarget.isBeforeFirst()) {
				request.setAttribute("isPassOK", "No");
				RequestDispatcher rd = request.getRequestDispatcher("transfer.jsp");
				rd.forward(request, response);
			} else {
				// compare username, if diff and session username is not admin, forward
				// permission deny
				// to transfer.jsp
				String username2 = null;
				while (rstTarget.next()) {
					username2 = rstTarget.getString(8);
				}
				AccountModel ac = (AccountModel) request.getSession().getAttribute("userDetails");
				String username1 = ac.getUsername();
				if (!username1.equals(username2) && !username1.equals("admin")) {
					request.setAttribute("error", "Permission deny: user can only tranfer between own accounts");
					RequestDispatcher rd = request.getRequestDispatcher("transfer.jsp");
					rd.forward(request, response);
				}

				System.out.println("I am in");
				// readwritelock for admin when normal users write and admin write and read.
				// readwritelock for normal user when normal users read and admin write.
				// Then normal users can always read until admin write.
				// admin read will always wait until no one write.
				try {
					if (username1.equals("admin")) {
						adminReadLock.lock();
						userReadLock.lock();
					} else {
						adminReadLock.lock();
					}
					ResultSet rs1 = stmt.executeQuery("select * from amount where id ='" + account_no + "'");

					while (rs1.next()) {
						own_amount = rs1.getInt(2);
					}

					if (own_amount >= transfer_amount) {
						own_amount -= transfer_amount;

						ResultSet rs2 = stmt.executeQuery("select * from amount where id ='" + target_acc_no + "'");

						while (rs2.next()) {
							recipient_amount = rs2.getInt(2);
						}

						recipient_amount += transfer_amount;
					} else {
						request.setAttribute("EnoughMoney", "No");
						RequestDispatcher rd = request.getRequestDispatcher("transfer.jsp");
						rd.forward(request, response);
					}
				} finally {
					if (username1.equals("admin")) {
						adminReadLock.unlock();
						userReadLock.unlock();
					} else {
						adminReadLock.unlock();
					}
				}

				try {
					if (own_amount >= transfer_amount) {
						if (username1.equals("admin")) {
							adminWriteLock.lock();
						} else {
							userWriteLock.lock();
						}
						PreparedStatement ps = conn.prepareStatement("update amount set amount=? where id= ?");
						ps.setInt(1, own_amount);
						ps.setString(2, account_no);
						ps.executeUpdate();

						PreparedStatement ps1 = conn.prepareStatement("update amount set amount=? where id= ?");
						ps1.setInt(1, recipient_amount);
						ps1.setString(2, target_acc_no);
						ps1.executeUpdate();
					}
				} finally {
					if (username1.equals("admin")) {
						adminWriteLock.unlock();
					} else {
						userWriteLock.unlock();
					}
				}
				if (own_amount >= transfer_amount) {
					RequestDispatcher rd = request.getRequestDispatcher("transfer_process.jsp");
					rd.forward(request, response);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

}
