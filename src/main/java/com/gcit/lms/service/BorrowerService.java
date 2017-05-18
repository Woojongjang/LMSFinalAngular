package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;

public class BorrowerService {
	@Autowired
	BorrowerDAO brdao;
	
	@Autowired
	BookLoanDAO bldao;
	
	public boolean checkBorrowerId(Integer borrowerID) {
		try {
			Borrower borrower = brdao.readBorrowerByID(borrowerID);
			if(borrower == null) {
				return false;
			}
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Borrower getBorrowerById(Integer borrowerID) {
		try {
			Borrower borrower = brdao.readBorrowerByID(borrowerID);
			return borrower;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void returnBookLoan(BookLoan loan) {
		try {
			bldao.returnBookLoan(loan);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
