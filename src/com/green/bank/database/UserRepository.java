package com.green.bank.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import com.green.bank.model.AccountModel;
import com.green.bank.util.AccountInvalidException;
import com.green.bank.util.DatabaseException;

public class UserRepository {

	public Optional<AccountModel> checkUserExist(String username, String password) throws DatabaseException {

		try {
			Statement stmt;
			Connection conn = JDBC_Connect.getConnection();
			AccountModel am = new AccountModel();
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(
					"select * from account where username ='" + username + "'" + "and password='" + password + "'");
			if (!rs.isBeforeFirst()) {
				return Optional.empty();
			} else {
				if (rs.next()) {

					// Setting all variables to model class
					am.setAccount_no(rs.getString(1));
					am.setFirst_name(rs.getString(2));
					am.setLast_name(rs.getString(3));
					am.setAddress(rs.getString(4));
					am.setCity(rs.getString(5));
					am.setBranch(rs.getString(6));
					am.setZip(rs.getString(7));
					am.setUsername(rs.getString(8));
					am.setPassword(rs.getString(9));
					am.setPhone_number(rs.getString(10));
					am.setEmail(rs.getString(11));
					am.setAccount_type(rs.getString(12));
					am.setReg_date(rs.getString(13));

					ResultSet rs1 = stmt.executeQuery("select * from amount where id ='" + am.getAccount_no() + "'");

					if (rs1.next()) {
						am.setAmount(rs1.getInt(2));
					}
					return Optional.of(am);
				}
			}
		} catch (AccountInvalidException | SQLException e) {
			return Optional.empty();
		}
		return Optional.empty();
	}
}
