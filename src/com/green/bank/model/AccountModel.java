package com.green.bank.model;

import com.green.bank.util.AccountInvalidException;

public class AccountModel {
	private String account_no, first_name, last_name, address, city, branch, zip, username, password, phone_number,
			email, account_type, reg_date;
	private int amount;

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) throws AccountInvalidException {
		if (account_no == null || account_no.length() <= 0) {
			throw new AccountInvalidException("Account number cannot be empty");
		}
		this.account_no = account_no;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) throws AccountInvalidException {
		if (first_name == null || first_name.length() <= 2) {
			throw new AccountInvalidException("First name cannot be empty");
		}
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) throws AccountInvalidException {
		if (last_name == null || last_name.length() < 1) {
			throw new AccountInvalidException("Last name cannot be empty");
		}
		this.last_name = last_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) throws AccountInvalidException {
		if (address == null || address.length() < 3) {
			throw new AccountInvalidException("Address cannot be empty, must be atleast 10 chars");
		}
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) throws AccountInvalidException {
		if (city == null || city.length() < 2) {
			throw new AccountInvalidException("City cannot be empty, must be atleast 2 characters");
		}
		this.city = city;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) throws AccountInvalidException {
		if (branch == null || branch.length() < 2) {
			throw new AccountInvalidException("Branch cannot be empty, must be atleast 2 characters");
		}
		this.branch = branch;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) throws AccountInvalidException {
		if (zip == null || zip.length() < 3) {
			throw new AccountInvalidException("Zip cannot be empty, must be atleast 5 characters");
		}
		this.zip = zip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) throws AccountInvalidException {
		if (username == null || username.length() < 3) {
			throw new AccountInvalidException("Username cannot be empty, must be atleast 5 characters");
		}
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws AccountInvalidException {
		if (password == null || password.length() < 3) {
			// TODO matcher pattern for a regex: 1 special character, 1 numeric, 1 letter
			// atleast
			throw new AccountInvalidException("password cannot be empty, must be atleast 5 characters");
		}
		this.password = password;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) throws AccountInvalidException {
		if (phone_number == null || phone_number.length() < 3) {
			// TODO matcher pattern for a regex: only digits, atleast 10
			throw new AccountInvalidException("phone number cannot be empty, must be atleast 10 digits");
		}
		this.phone_number = phone_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws AccountInvalidException {
		if (email == null || email.length() < 3) {
			// regex for @..
			throw new AccountInvalidException("email cannot be empty, must be atleast 5 characters");
		}
		this.email = email;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) throws AccountInvalidException {
		if (account_type == null || account_type.length() < 3) {
			// regex for @..
			throw new AccountInvalidException("account_type cannot be empty, must be atleast 5 characters");
		}
		this.account_type = account_type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) throws AccountInvalidException {
		if (amount <= 0) {
			// regex for all digits
			throw new AccountInvalidException("Amount should be greater than 500");
		}
		this.amount = amount;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
}