package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.LibraryBranch;
import com.gcit.lms.entity.Publisher;
/**
 * This is a DAO
 * @branch woojong
 *
 */
public class LibraryBranchDAO extends BaseDAO implements ResultSetExtractor<List<LibraryBranch>>{
	
	public void addBranch(LibraryBranch branch) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_library_branch (branchName, branchAddress) values (?,?)",
				new Object[] {branch.getBranchName(),branch.getBranchAddress()});
	}
	
	public void updateBranch(LibraryBranch branch) throws ClassNotFoundException, SQLException{
		template.update("update tbl_library_branch set branchName = ?, branchAddress = ? where branchId = ?",
				new Object[]{branch.getBranchName(), branch.getBranchAddress(), branch.getBranchId()});
	}
	
	public void deleteBranch(LibraryBranch branch) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_library_branch where branchId = ?", new Object[] {branch.getBranchId()});
	}
	
	public List<LibraryBranch> readAllBranches(Integer pageNo) throws ClassNotFoundException, SQLException{
		String query = "select * from tbl_library_branch";
		if (pageNo != null) {
			setPageSize(10);
			setPageNo(pageNo);
			query = setLimit(query);
		}
		return template.query(query, new Object[]{}, this);
	}
	
	public LibraryBranch readBranchByID(Integer branchID) throws ClassNotFoundException, SQLException{
		List<LibraryBranch> branches = template.query("select * from tbl_library_branch where branchId = ?", new Object[]{branchID}, this);
		if(branches!=null && !branches.isEmpty()){
			return branches.get(0);
		}
		return null;
	}
	
//	public List<Book> getBooksNotInBranch(Integer  branchId) throws ClassNotFoundException, SQLException{
//		BookDAO bdao = new BookDAO(conn);
//		return bdao.template.query("select * from tbl_book where bookId not in (select bookId from tbl_book_copies where branchId = ?)", new Object[]{branchId});
//	}
	
	public List<LibraryBranch> readBranchesByName(String  branchName) throws ClassNotFoundException, SQLException{
		setPageSize(10);
		branchName = "%"+branchName+"%";
		return template.query("select * from tbl_library_branch where branchName like ?", new Object[]{branchName}, this);
	}
	
	public List<LibraryBranch> readBranchesByName(Integer pageNo, String  branchName) throws ClassNotFoundException, SQLException{
		String query = "select * from tbl_library_branch where branchName like ?";
		if (pageNo != null) {
			setPageSize(10);
			setPageNo(pageNo);
			query = setLimit(query);
		}
		branchName = "%"+branchName+"%";
		List<LibraryBranch> retList = template.query(query, new Object[]{branchName}, this);
		return retList;
	}
	
//	public Integer getBranchesCount() throws ClassNotFoundException, SQLException{
//		return readCount("select count(*) as COUNT from tbl_library_branch", null);
//	}
//	
//	public Integer getBranchBookCount(LibraryBranch branch) throws ClassNotFoundException, SQLException{
//		return readCount("select count(*) as COUNT from tbl_book_copies where branchId = ?"
//				, new Object[]{branch.getBranchId()});
//	}
	
//	public List<Book> readBranchBooksById(Integer pageNo, String search, Integer branchId) throws ClassNotFoundException, SQLException {
//		List<Book> books = new ArrayList<>();
//		BookDAO bdao = new BookDAO(conn);
//		bdao.setPageNo(pageNo);
//		bdao.setPageSize(10);
//		books = bdao.template.query("select * from tbl_book where bookId in (select bookId from tbl_book_copies where branchId = ?)", new Object[]{branchId}, this);
//		return books;
//	}

	public void updateBranchBooks(Integer branchId, Integer bookId, Integer count) throws ClassNotFoundException, SQLException{
		template.update("update tbl_book_copies set noOfCopies = ? where bookId = ? and branchId = ?",
				new Object[]{count, bookId, branchId});
	}
	
	public void updateOrCreateCount(Integer branchId, Integer bookId, Integer count) throws ClassNotFoundException, SQLException{
		List<LibraryBranch> counts = template.query("select * from tbl_library_branch where branchId in (select branchId from tbl_book_copies where branchId = ? and bookId = ?)",
				new Object[] { branchId, bookId }, this);
		if (counts != null && counts.size() > 0) {
			updateBranchBooks(branchId, bookId, count);
		} else {
			addBranchBooks(branchId, bookId, count);
		}
	}
	
	public void addBranchBooks(Integer branchId, Integer bookId, Integer count) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?,?,?)",
				new Object[]{bookId, branchId, count});
	}
	
	public void deleteBranchBooks(Integer branchId, Integer bookId) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_book_copies where branchId = ? and bookId = ?", new Object[] {branchId, bookId});
	}
	
	@Override
	public List<LibraryBranch> extractData(ResultSet rs) throws SQLException {
		List<LibraryBranch> branches = new ArrayList<>();
		HashMap<Book, Integer> booksCount = new HashMap<>();
		Integer count;
		Book book;
		while(rs.next()){
			LibraryBranch p = new LibraryBranch();
			p.setBranchId(rs.getInt("branchId"));
			String name = rs.getString("branchName");
			String addr = rs.getString("branchAddress");
			if(name == null) {
				p.setBranchName("NO LIBRARY BRANCH NAME");
			}
			else if(name.equals("")){
				p.setBranchName("NO LIBRARY BRANCH NAME");
			}
			else {
				p.setBranchName(name);
			}
			if(addr == null) {
				p.setBranchAddress("NO LIBRARY BRANCH ADDRESS");
			}
			else if(addr.equals("")){
				p.setBranchAddress("NO LIBRARY BRANCH ADDRESS");
			}
			else {
				p.setBranchAddress(addr);
			}
			branches.add(p);
		}
		return branches;
	}
}
