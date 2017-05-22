package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.BookBranchCountDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.LibraryBranch;

@RestController
public class LibrarianService {
	
	@Autowired
	LibraryBranchDAO lbdao;
	
	@Autowired
	BookDAO bdao;
	
	@Autowired
	BookBranchCountDAO bbcdao;
	
	@RequestMapping(value = "/Libraries/{branchId}/Books", method = RequestMethod.GET, produces = "application/json")
	public LibraryBranch getAllBranchBook(@PathVariable Integer branchId) {
		LibraryBranch branch = new LibraryBranch();
		try {
			branch = lbdao.readBranchByID(branchId);
			branch.setBooks(bdao.readBookByBranchId(branchId));
			branch.setBooksCount(bbcdao.readBookCountByBranch(branchId));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return branch;
	}
	
	@Transactional
	@RequestMapping(value = "/Libraries/{branchId}/{bookId}/{count}", method = RequestMethod.PUT, consumes = "application/json", produces="text/plain")
	public String addBranchBookCount(@PathVariable Integer branchId, @PathVariable Integer bookId, @PathVariable Integer count) {
		try {
			lbdao.updateOrCreateCount(branchId, bookId, count);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "BOOK COUNT UPDATED";
	}
	
	@Transactional
	@RequestMapping(value = "/Libraries/{branchId}/{bookId}", method = RequestMethod.DELETE, produces="text/plain")
	public String deleteBook(@PathVariable Integer branchId, @PathVariable Integer bookId) {
		try {
			lbdao.deleteBranchBooks(branchId, bookId);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "BOOK DELETED";
	}
	
	public Integer getBranchBookCount(LibraryBranch branch) {
		//TODO : CHANGE METHOD
		try {
			return lbdao.readBranchByID(branch.getBranchId()).getBooksCount().size();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public List<Book> getBooksNotInBranch(Integer branchId) {
		try {
			List<Book> books = bdao.readBookNotInBranchId(branchId);
			return books;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void addNewBranchBook(Integer bookId, Integer branchId, Integer count) {
		try {
			lbdao.addBranchBooks(bookId,branchId,count);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<Book,Integer> getLibraryBookSearch(Integer pageNoThenCount, String search, Integer branchId) {
		try {
			List<LibraryBranch> branchList = lbdao.readBranchesByName(pageNoThenCount, search);
			return null;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
