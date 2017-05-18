package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;

public class BookLoanService {
	@Autowired
	BookLoanDAO bldao;
	
	public Integer getBookLoansCount() {
		try {
			return bldao.readAllBookLoans(null).size();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getBookLoansCount(Integer borrowerId) {
		Connection conn = null;
		try {
			return bldao.readBookLoanByID(null, borrowerId).size();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void addBookLoan(BookLoan loan) {
		try {
			bldao.addBookLoanAutoDue(loan);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void editBookLoan(BookLoan loan) {
		try {
			bldao.updateBookLoan(loan);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<BookLoan> getAllBookLoans(Integer pageNo) {
		try {
			return bldao.readAllBookLoans(pageNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<BookLoan> getUserBookLoans(Integer pageNo, Borrower borrower) {
		try {
			return bldao.readBookLoanByID(pageNo, borrower.getBorrowerId());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteBookLoan(BookLoan loan) {
		try {
			bldao.deleteBookLoan(loan);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * @param pageNo
	 * @param searchString
	 * @return
	 */
	public List<BookLoan> getBookLoanByName(Integer pageNoThenCount, String search) {
		try {
			List<BookLoan> loanList = bldao.readBookLoansByString(pageNoThenCount, search);
			return loanList;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
