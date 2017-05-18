package com.gcit.lms.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.LibraryBranch;
import com.gcit.lms.entity.Publisher;

public class BookLoanDAO extends BaseDAO implements ResultSetExtractor<List<BookLoan>>{
	
	public void addBookLoan(BookLoan book) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_book_loans (bookId,branchId,cardNo,dateOut,dueDate,dateIn) values (?,?,?,?,?,?)",
				new Object[] {book.getBook().getBookId(),book.getBranch().getBranchId(),
						book.getBorrower().getBorrowerId(),book.getDateChecked(),book.getDateDue(),book.getDateIn()});
	}
	
	public void addBookLoanAutoDue(BookLoan loan) throws ClassNotFoundException, SQLException{
		template.update("{call insert_book_loans(?, ?, ?, ?)}", loan.getBook().getBookId(), loan.getBranch().getBranchId(),
				loan.getBorrower().getBorrowerId(), loan.getDateChecked());
	}
	
	public void addBookLoanGenres(Integer bookId, Integer genreId) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_book_genres values (?, ?)", new Object[] {genreId, bookId});
	}
	
	public void addBookLoanPublisher(Integer bookId, Integer pubId) throws ClassNotFoundException, SQLException{
		template.update("update tbl_book set pubId = ? where bookId = ?", new Object[] {pubId, bookId});
	}
	
//	public void deleteBookLoanAuthors(BookLoan book) throws ClassNotFoundException, SQLException{
//		template.update("delete from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ? and dateOut = ?;", 
//				new Object[] {book.getBook().getBookId(), book.getBranch().getBranchId(),
//						book.getBorrower().getBorrowerId(), book.getDateChecked()});
//	}
	
	public void deleteBookLoanGenres(Integer bookId) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_book_genres where bookId = ?", new Object[] {bookId});
	}
	
	public void updateBookLoan(BookLoan book) throws ClassNotFoundException, SQLException{
		Integer bookId = book.getBook().getBookId();
		Integer branchId = book.getBranch().getBranchId();
		Integer borrowId = book.getBorrower().getBorrowerId();
		String dateChecked = book.getDateChecked();
		
		String dateDue = book.getDateDue();
		String dateIn = book.getDateIn();
		template.update("update tbl_book_loans set dueDate = ?, set dateIn = ? where bookId = ? and branchId = ? and cardNo = ? and dateOut = ?",
				new Object[]{dateDue, dateIn, bookId, branchId, borrowId, dateChecked});
	}
	
	public void returnBookLoan(BookLoan book) throws ClassNotFoundException, SQLException{
		Integer bookId = book.getBook().getBookId();
		Integer branchId = book.getBranch().getBranchId();
		Integer borrowId = book.getBorrower().getBorrowerId();
		String dateChecked = book.getDateChecked();
		
		String dateIn = book.getDateIn();
		template.update("update tbl_book_loans set dateIn = ? where bookId = ? and branchId = ? and cardNo = ? and dateOut = ?",
				new Object[]{dateIn, bookId, branchId, borrowId, dateChecked});
	}
	
	public void deleteBookLoan(BookLoan book) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ? and dateOut = ?",
				new Object[] {book.getBook().getBookId(), book.getBranch().getBranchId(),
						book.getBorrower().getBorrowerId(), book.getDateChecked()});
	}
	
	public List<BookLoan> readBookLoanByID(Integer pageNo, Integer borrowerId) throws ClassNotFoundException, SQLException{
		if(pageNo!=null) {
			setPageSize(10);
			setPageNo(pageNo);
		}
		List<BookLoan> books = template.query("select * from tbl_book_loans where cardNo = ?", new Object[]{borrowerId}, this);
		return books;
	}
	
	public List<BookLoan> readBookLoansByString(Integer pageNo, String search) throws ClassNotFoundException, SQLException{
		if(pageNo!=null) {
			setPageSize(10);
			setPageNo(pageNo);
		}
		search = "%"+search+"%";
		List<BookLoan> retList = template.query("select * from tbl_book_loans where (bookId like ? or branchId like ? or cardNo like ? or dateOut like ? or dueDate like ? or dateIn like ?)"
				+" or bookId IN (select bookId from tbl_book where title like ?) or branchId in (select branchId from tbl_library_branch where branchName like ?)"
				+" or cardNo In (select cardNo from tbl_borrower where name like ?)",
				new Object[]{search, search, search, search, search, search, search, search, search}, this);
		return retList;
	}
	
	public List<BookLoan> readAllBookLoans(Integer pageNo) throws ClassNotFoundException, SQLException{
		if(pageNo!=null) {
			setPageNo(pageNo);
			setPageSize(10);
		}
		return template.query("select * from tbl_book_loans", new Object[]{}, this);
	}

//	public Integer getBookLoansCount() throws ClassNotFoundException, SQLException{
//		return readCount("select count(*) as COUNT from tbl_book_loans", null);
//	}
//	
//	public Integer getBookLoansCount(Integer borrowerId) throws ClassNotFoundException, SQLException{
//		return readCount("select count(*) as COUNT from tbl_book_loans where cardNo = ?", new Object[]{borrowerId});
//	}
	
	@Override
	public List<BookLoan> extractData(ResultSet rs) throws SQLException {
		List<BookLoan> books = new ArrayList<>();
		while(rs.next()){
			String dateChecked = rs.getString("dateOut");
			
			String dateDue = rs.getString("dueDate");
			String dateIn = rs.getString("dateIn");
			BookLoan bookLoan = new BookLoan();
			bookLoan.setDateChecked(dateChecked);
			if(dateDue != null) {
				bookLoan.setDateDue(dateDue);
			}
			if(dateIn!=null) {
				bookLoan.setDateIn(dateIn);
			}
			books.add(bookLoan);
		}
		return books;
	}
	
}
