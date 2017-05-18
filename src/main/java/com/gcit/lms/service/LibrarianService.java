package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.LibraryBranch;

public class LibrarianService {
	
	@Autowired
	LibraryBranchDAO lbdao;
	
	@Autowired
	BookDAO bdao;
	
	public LibraryBranch getBranchById(Integer branchId) {
		try {
			return lbdao.readBranchByID(branchId);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
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
	
	public void addBranchBookCount(Integer bookId, Integer branchId, Integer count) {
		Connection conn = null;
		try {
			lbdao.updateBranchBooks(bookId,branchId,count);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
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
