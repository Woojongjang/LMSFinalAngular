package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.PlatformTransactionManager;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookBranchCount;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;
//import com.mysql.jdbc.PreparedStatement;

public class BookBranchCountDAO extends BaseDAO implements ResultSetExtractor<List<BookBranchCount>>{

	public void addBook(Book book) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_book (title) values (?)", new Object[] {book.getTitle()});
	}
	

	public HashMap<Integer, Integer> readBookCountByBranch(Integer branchID) throws ClassNotFoundException, SQLException{
		List<BookBranchCount> bookBranchCount = template.query("select * from tbl_book_copies where branchId = ?", new Object[]{branchID}, this);
		HashMap<Integer, Integer> bookCount = new HashMap<Integer, Integer>();
		for(BookBranchCount bbc : bookBranchCount) {
			bookCount.put(bbc.getBookId(), bbc.getCount());
		}
		return bookCount;
	}
	
	public List<BookBranchCount> readBookBranchObj(Integer branchID) throws ClassNotFoundException, SQLException{
		List<BookBranchCount> bookBranchCount = template.query("select * from tbl_book_copies where branchId = ?", new Object[]{branchID}, this);
		return bookBranchCount;
	}
	
	@Override
	public List<BookBranchCount> extractData(ResultSet rs) throws SQLException {
		List<BookBranchCount> bookBranchList = new ArrayList<>();
		while(rs.next()){
			BookBranchCount b = new BookBranchCount();
			b.setBookId(rs.getInt("bookId"));
			b.setBranchId(rs.getInt("branchId"));
			b.setCount(rs.getInt("noOfCopies"));
			bookBranchList.add(b);
		}
		return bookBranchList;
	}
}
