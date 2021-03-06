package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.LibraryBranch;

@RestController
public class BorrowerService {
	@Autowired
	BorrowerDAO brdao;
	
	@Autowired
	BookLoanDAO bldao;
	
	@Autowired
	BookDAO bdao;
	
	@Autowired
	LibraryBranchDAO lbdao;
	
	
	@RequestMapping(value = "/initBookLoan", method = RequestMethod.GET, produces="application/json")
	public BookLoan initBookLoan() {
		BookLoan loan = new BookLoan();
		Book book = new Book();
		Borrower borrower = new Borrower();
		LibraryBranch branch = new LibraryBranch();
		loan.setBook(book);
		loan.setBorrower(borrower);
		loan.setBranch(branch);
		return loan;
	}
	
	
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
	
	
	@RequestMapping(value = "/Borrower/{borrowerId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getBorrowerById(@PathVariable Integer borrowerId) {
		Borrower borrower;
		try {
			borrower = brdao.readBorrowerByID(borrowerId);
			// Set BookLoans list for User here or in: /Borrower/{borrowerId}/BookLoans
//			borrower.setBookLoans(bldao.readBookLoanByID(null, borrowerId));
		} catch (ClassNotFoundException | SQLException e) {
			//e.printStackTrace();
			String errorMessage;
	        errorMessage = e + " <== error";
	        String JsonError = "{\"error\":"+"\""+errorMessage+"\"" +"}";
			return new ResponseEntity<>(JsonError, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Borrower>(borrower,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/Borrower/{borrowerId}/BookLoans", method = RequestMethod.GET, produces = "application/json")
	public List<BookLoan> getBorrowerBooksById(@PathVariable Integer borrowerId) {
		List<BookLoan> bookLoans = new ArrayList<BookLoan>();
		try {
			bookLoans = bldao.readBookLoanByID(null, borrowerId);
			
			for(BookLoan loans : bookLoans) {
				loans.setBook(bdao.readBookByID(loans.getBook().getBookId()));
				loans.setBorrower(brdao.readBorrowerByID(borrowerId));
				loans.setBranch(lbdao.readBranchByID(loans.getBranch().getBranchId()));
			}
			return bookLoans;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	@RequestMapping(value = "/Borrower/{borrowerId}/BookLoans", method = RequestMethod.PUT, consumes="application/json", produces="text/plain")
	public String returnBookLoan(@PathVariable Integer borrowerId, @RequestBody BookLoan loan) {
		try {
			loan.getBorrower().setBorrowerId(borrowerId);
			bldao.returnBookLoan(loan);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "BOOK RETURNED";
	}
	
	@Transactional
	@RequestMapping(value = "/Borrower/{borrowerId}/BookLoans", method = RequestMethod.POST, consumes="application/json", produces="text/plain")
	public String addBookLoan(@PathVariable Integer borrowerId, @RequestBody BookLoan loan) {
		try {
			loan.getBorrower().setBorrowerId(borrowerId);
			bldao.addBookLoanAutoDue(loan);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "BOOK BORROWED";
	}
	
	public void returnBookLoan(BookLoan loan) {
		try {
			bldao.returnBookLoan(loan);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
