package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

public class BookDAO extends BaseDAO implements ResultSetExtractor<List<Book>>{

	public void addBook(Book book) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_book (title) values (?)", new Object[] {book.getTitle()}, this);
	}
	
	public Integer addBookWithID(Book book) throws ClassNotFoundException, SQLException{
		//KeyHolder holder = new GeneratedKeyHolder();
		//template.
		return template.update("insert into tbl_book (title) values (?)", new Object[] {book.getTitle()}, this);
	}
	
	public void addBookAuthors(Integer bookId, Integer authorId) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_book_authors values (?, ?)", new Object[] {bookId, authorId}, this);
	}
	
	public void addBookGenres(Integer bookId, Integer genreId) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_book_genres values (?, ?)", new Object[] {genreId, bookId}, this);
	}
	
	public void addBookPublisher(Integer bookId, Integer pubId) throws ClassNotFoundException, SQLException{
		template.update("update tbl_book set pubId = ? where bookId = ?", new Object[] {pubId, bookId}, this);
	}
	
	public void deleteBookAuthors(Integer bookId) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_book_authors where bookId = ?", new Object[] {bookId}, this);
	}
	
	public void deleteBookGenres(Integer bookId) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_book_genres where bookId = ?", new Object[] {bookId}, this);
	}
	
	public void updateBook(Book book) throws ClassNotFoundException, SQLException{
		List<Author> bookAuthors = book.getAuthors();
		List<Genre> bookGenres = book.getGenres();
		if(bookAuthors!=null && !bookAuthors.isEmpty()) {
			deleteBookAuthors(book.getBookId());
			for(Author bAuthElem : bookAuthors) {
				addBookAuthors(book.getBookId(),bAuthElem.getAuthorId());
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
//			System.out.println("UPDATING PUB ID:" +book.getPublisher().getPublisherId());
//			System.out.println("UPDATING BOOK ID:" +book.getBookId());
			template.update("update tbl_book set pubId = ? where bookId = ?",
					new Object[]{book.getPublisher().getPublisherId(), book.getBookId()}, this);
		}
		else {
			template.update("update tbl_book set pubId = ? where bookId = ?",
					new Object[]{null, book.getBookId()}, this);
		}
		template.update("update tbl_book set title = ? where bookId = ?",
				new Object[]{book.getTitle(), book.getBookId()}, this);
	}
	
	public void deleteBook(Book book) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_book where bookId = ?", new Object[] {book.getBookId()}, this);
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
	
	public List<Book> readBooksByName(Integer pageNo, String  bookName) throws ClassNotFoundException, SQLException{
		if(pageNo!=null) {
			setPageSize(10);
			setPageNo(pageNo);
		}
		bookName = "%"+bookName+"%";
		List<Book> retList = template.query("select * from tbl_book where title like ?", new Object[]{bookName}, this);
		return retList;
	}
	
	public List<Book> readAllBooks(Integer pageNo) throws ClassNotFoundException, SQLException{
		if(pageNo!=null) {
			setPageNo(pageNo);
			setPageSize(10);
		}
		//List<Book> bookRead = template.query("select * from tbl_book", null);
		return template.query("select * from tbl_book", new Object[]{}, this);
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
