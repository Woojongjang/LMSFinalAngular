package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.PlatformTransactionManager;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;
//import com.mysql.jdbc.PreparedStatement;

public class BookDAO extends BaseDAO implements ResultSetExtractor<List<Book>>{

	public void addBook(Book book) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_book (title) values (?)", new Object[] {book.getTitle()});
	}
	
	public Integer addBookWithID(Book book) throws ClassNotFoundException, SQLException{
		final String INSERT_SQL = "insert into tbl_book (title) values (?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		        	PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] {"bookId"});
		            ps.setString(1, book.getTitle());
		            return ps;
		        }
		    },
		    keyHolder);
		return keyHolder.getKey().intValue();
		//return template.update("insert into tbl_book (title) values (?)", new Object[] {book.getTitle()});
	}
	
	public void addBookAuthors(Book book, Author author) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_book_authors values (?, ?)", new Object[] {book.getBookId(), author.getAuthorId()});
	}
	
	public void addBookAuthorInteger(Integer bookId, Integer authorId) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_book_authors (bookId, authorId) values (?, ?)", new Object[] {bookId, authorId});
	}
	
	public void addBookGenres(Integer bookId, Integer genreId) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_book_genres values (?, ?)", new Object[] {genreId, bookId});
	}
	
	public void addBookPublisher(Integer bookId, Integer pubId) throws ClassNotFoundException, SQLException{
		template.update("update tbl_book set pubId = ? where bookId = ?", new Object[] {pubId, bookId});
	}
	
	public void deleteBookAuthors(Integer bookId) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_book_authors where bookId = ?", new Object[] {bookId});
	}
	
	public void deleteBookGenres(Integer bookId) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_book_genres where bookId = ?", new Object[] {bookId});
	}
	
	public void updateBook(Book book) throws ClassNotFoundException, SQLException{
		List<Author> bookAuthors = book.getAuthors();
		List<Genre> bookGenres = book.getGenres();
		if(bookAuthors!=null && !bookAuthors.isEmpty()) {
			deleteBookAuthors(book.getBookId());
			for(Author bAuthElem : bookAuthors) {
				addBookAuthors(book,bAuthElem);
			}
		}
		else {
			deleteBookAuthors(book.getBookId());
		}
		if(bookGenres!=null && !bookGenres.isEmpty()) {
			deleteBookGenres(book.getBookId());
			for(Genre bGenreElem : bookGenres) {
				addBookGenres(book.getBookId(),bGenreElem.getGenreId());
			}
		}
		else {
			deleteBookGenres(book.getBookId());
		}
		if(book.getPublisher()!=null) {
			if(book.getPublisher().getPublisherId()!=null && book.getPublisher().getPublisherId()>0){
				template.update("update tbl_book set pubId = ? where bookId = ?",
						new Object[]{book.getPublisher().getPublisherId(), book.getBookId()});
			}
		}
		else {
			template.update("update tbl_book set pubId = ? where bookId = ?",
					new Object[]{null, book.getBookId()});
		}
		template.update("update tbl_book set title = ? where bookId = ?",
				new Object[]{book.getTitle(), book.getBookId()});
	}
	
	public void updateOrCreateBook(Book book) throws ClassNotFoundException, SQLException {
		List<Book> books = template.query("select * from tbl_book where bookId = ?",
				new Object[] { book.getBookId() }, this);
		if (books != null && books.size() > 0) {
			updateBook(book);
		} else {
			Integer bookId = addBookWithID(book);
			List<Author> bookAuthors = book.getAuthors();
			List<Genre> bookGenres = book.getGenres();
			Publisher bookPublisher = book.getPublisher();
			if(bookAuthors!=null && !bookAuthors.isEmpty()) {
				for(Author bAuthElem : bookAuthors) {
					addBookAuthors(book,bAuthElem);
				}
			}
			if(bookGenres!=null && !bookGenres.isEmpty()) {
				for(Genre bGenreElem : bookGenres) {
					addBookGenres(bookId,bGenreElem.getGenreId());
				}
			}
			if(bookPublisher!=null) {
				addBookPublisher(bookId,bookPublisher.getPublisherId());
			}
		}
	}
	
	public void deleteBook(Book book) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_book where bookId = ?", new Object[] {book.getBookId()});
	}
	
	public Book readBookByID(Integer bookID) throws ClassNotFoundException, SQLException{
		List<Book> books = template.query("select * from tbl_book where bookId = ?", new Object[]{bookID}, this);
		if(books!=null && !books.isEmpty()){
			return books.get(0);
		}
		return null;
	}
	
	public List<Book> readBookByBranchId(Integer branchID) throws ClassNotFoundException, SQLException{
		List<Book> books = template.query("select * from tbl_book where bookId in (select bookId from tbl_book_copies where branchId = ?)", new Object[]{branchID}, this);
		return books;
	}
	
	public List<Book> readBookNotInBranchId(Integer branchID) throws ClassNotFoundException, SQLException{
		List<Book> books = template.query("select * from tbl_book where bookId in (select bookId from tbl_book_copies where branchId <> ?)", new Object[]{branchID}, this);
		return books;
	}
	
	public List<Book> readBookByUserId(Integer borrowerID) throws ClassNotFoundException, SQLException{
		List<Book> books = template.query("select * from tbl_book where bookId in (select bookId from tbl_book_loans where cardNo = ?)", new Object[]{borrowerID}, this);
		return books;
	}
	
	public List<Book> readBooksByName(Integer pageNo, String  bookName) throws ClassNotFoundException, SQLException{
		String query = "select * from tbl_book where title like ?";
		if(pageNo != null) {
			setPageSize(10);
			setPageNo(pageNo);
			query = setLimit(query);
		}
		bookName = "%"+bookName+"%";
		List<Book> retList = template.query(query, new Object[]{bookName}, this);
		return retList;
	}
	
	public List<Book> readAllBooks(Integer pageNo) throws ClassNotFoundException, SQLException{
		String query = "select * from tbl_book";
		if(pageNo != null) {
			setPageSize(10);
			setPageNo(pageNo);
			query = setLimit(query);
		}
		//List<Book> bookRead = template.query("select * from tbl_book", null);
		return template.query(query, new Object[]{}, this);
	}

//	public Integer getBooksCount() throws ClassNotFoundException, SQLException{
//		return readCount("select count(*) as COUNT from tbl_book", null);
//	}
	
	public List<Book> readAllBooksByAuthorID(Integer authorId){
		return template.query("select * from tbl_book where bookId IN (Select bookId from tbl_book_authors where authorId = ?)", new Object[]{authorId}, this);
	}
	
	public List<Book> readAllBooksByGenreID(Integer genreId){
		return template.query("select * from tbl_book where bookId IN (Select bookId from tbl_book_genres where genre_id = ?)", new Object[]{genreId}, this);
	}
	
	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();
		while(rs.next()){
			Book b = new Book();
			b.setTitle(rs.getString("title"));
			b.setBookId(rs.getInt("bookId"));
			books.add(b);
		}
		return books;
	}
}
