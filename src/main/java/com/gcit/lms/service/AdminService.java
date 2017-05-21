package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.LibraryBranch;
import com.gcit.lms.entity.Publisher;

@RestController
public class AdminService {
	@Autowired
	AuthorDAO adao;
	
	@Autowired
	BookDAO bdao;
	
	@Autowired
	PublisherDAO pdao;
	
	@Autowired
	GenreDAO gdao;
	
	@Autowired
	LibraryBranchDAO lbdao;
	
	@Autowired
	BorrowerDAO brdao;
	
	//-------------AUTHOR SERVICES-------------//
	
	@RequestMapping(value = "/initAuthor", method = RequestMethod.GET, produces="application/json")
	public Author initAuthor() {
		return new Author();
	}
	
	@Transactional
	@RequestMapping(value = "/Authors", method = RequestMethod.POST, consumes="application/json", produces="text/plain")
	public String addAuthor(@RequestBody Author author) {
		try {
			adao.addAuthor(author);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "AUTHOR ADDED";
	}
	
	// Post Update Author with known id 
//	@Transactional
//	@RequestMapping(value = "/Authors/{authorId}", method = RequestMethod.POST, consumes="application/json")
//	public String authorExistUpdate(@RequestBody Author author, @PathVariable Integer authorId) {
//		try {
//			// if authorId is an existing Id, have the http method update value
//			// if id is new (non-existing resource), have method return error
//			author.setAuthorId(authorId);
//			adao.updateAuthor(author);
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//		return "AUTHOR UPDATED";
//	}
	
	@RequestMapping(value = "/searchAuthors/{authorName}", method = RequestMethod.GET, produces="application/json")
	public List<Author> getAuthorsByName(@PathVariable String authorName) {

		try {
			return adao.readAuthorsByName(authorName);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/Authors", method = RequestMethod.GET, produces = "application/json")
	public List<Author> getAllAuthors(@RequestParam(value="pageNo",required=false) Integer pageNo, 
			@RequestParam(value = "searchString", required = false) String searchString) {
		List<Author> authors = new ArrayList<>();
		try {
			if (searchString!=null && searchString.length()>0) {
				authors = adao.readAuthorsByName(pageNo, searchString);
			} else 
			if (pageNo!=null&&pageNo>0) {
				authors = adao.readAllAuthors(pageNo);
			}
			else {
				authors = adao.readAllAuthors(null);
			}
			for (Author a : authors) {
				a.setBooks(bdao.readAllBooksByAuthorID(a.getAuthorId()));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return authors;
	}
	
	@RequestMapping(value = "/Authors/{authorId}", method = RequestMethod.GET, produces = "application/json")
	public Author getAuthorById(@PathVariable Integer authorId) {
		try {
			Author author = adao.readAuthorByID(authorId);
			author.setBooks(bdao.readAllBooksByAuthorID(authorId));
			return author;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	@RequestMapping(value = {"/Authors", "/Authors/{authorId}"}, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Author editCreateAuthor(@RequestBody Author author, @PathVariable Optional<Integer> authorId) {
		try {
			if(authorId.isPresent()) {
				author.setAuthorId(authorId.get());
			}
			adao.updateOrCreateAuthor(author);
			return adao.readAuthorByID(author.getAuthorId());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	@RequestMapping(value = "/Authors/{authorId}", method = RequestMethod.DELETE, produces="text/plain")
	public String deleteAuthor(@PathVariable Integer authorId) {
		try {
			Author author = new Author();
			author.setAuthorId(authorId);
			adao.deleteAuthor(author);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "AUTHOR DELETED";
	}

	//-------------BOOK SERVICES-------------//
	
	@RequestMapping(value = "/initBook", method = RequestMethod.GET, produces="application/json")
	public Book initBook() {
		return new Book();
	}
	
	@Transactional
	@RequestMapping(value = "/Books", method = RequestMethod.POST, consumes="application/json", produces = "application/json")
	public Book addBook(@RequestBody Book book) {
		try {
			// EDIT BookDAO for addBookWithID to work with returning generated key
			Integer bookId = bdao.addBookWithID(book);
//			List<Author> bookAuthors = book.getAuthors();
//			List<Genre> bookGenres = book.getGenres();
//			Publisher bookPublisher = book.getPublisher();
//			if(bookAuthors!=null && !bookAuthors.isEmpty()) {
//				for(Author bAuthElem : bookAuthors) {
//					bdao.addBookAuthors(bookId,bAuthElem.getAuthorId());
//				}
//			}
//			if(bookGenres!=null && !bookGenres.isEmpty()) {
//				for(Genre bGenreElem : bookGenres) {
//					bdao.addBookGenres(bookId,bGenreElem.getGenreId());
//				}
//			}
//			if(bookPublisher!=null) {
//				bdao.addBookPublisher(bookId,bookPublisher.getPublisherId());
//			}
			return book;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/Books", method = RequestMethod.GET, produces = "application/json")
	public List<Book> getAllBooks(@RequestParam(value="pageNo",required=false) Integer pageNo, 
			@RequestParam(value = "searchString", required = false) String searchString) {
		try {
			return bdao.readAllBooks(pageNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/Books/{bookId}", method = RequestMethod.GET, produces = "application/json")
	public Author getBookById(@PathVariable Integer authorId) {
		try {
			Author author = adao.readAuthorByID(authorId);
			author.setBooks(bdao.readAllBooksByAuthorID(authorId));
			return author;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public void editBook(Book book) {
		try {
			bdao.updateBook(book);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void deleteBook(Book book) {
		try {
			bdao.deleteBook(book);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void addGenre(Genre genre)  {
		try {
			gdao.addGenre(genre);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void editGenre(Genre genre) {
		try {
			gdao.updateGenre(genre);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void addPublisher(Publisher publisher) {
		try {
			pdao.addPublisher(publisher);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void editPublisher(Publisher publisher) {
		try {
			pdao.updatePublisher(publisher);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void addBorrower(Borrower borrower) {
		try {
			brdao.addBorrower(borrower);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void editBorrower(Borrower borrower) {
		try {
			brdao.updateBorrower(borrower);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void addBranch(LibraryBranch branch) {
		try {
			lbdao.addBranch(branch);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void editBranch(LibraryBranch branch) {
		try {
			lbdao.updateBranch(branch);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
//	@Transactional
//	@RequestMapping(value = "/Authors", method = RequestMethod.GET, produces = "application/json")
//	public List<Author> getAllAuthors() {
//		List<Author> authors = new ArrayList<>();
//		try {
//			authors = adao.readAllAuthors(null);
//			for (Author a : authors) {
//				a.setBooks(bdao.readAllBooksByAuthorID(a.getAuthorId()));
//			}
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//		return authors;
//	}

	@Transactional
	@RequestMapping(value="/Genres", method=RequestMethod.GET, produces="application/json")
	public List<Genre> getAllGenres() {
		List<Genre> genres = new ArrayList<Genre>();
		try {
			genres = gdao.readAllGenres(null);
			// Choose to show all books of each genres
//			for (Genre g : genres) {
//				g.setBooks(bdao.readAllBooksByGenreID(g.getGenreId()));
//			}
			return genres;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Publisher> getAllPublishers(Integer pageNo) {
		List<Publisher> publishers = new ArrayList<>();
		try {
			return pdao.readAllPublishers(pageNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Borrower> getAllBorrowers(Integer pageNo) {
		try {
			return brdao.readAllBorrowers(pageNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<LibraryBranch> getAllBranches(Integer pageNo) {
		try {
			return lbdao.readAllBranches(pageNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Author getAuthorByPk(Integer authorId) {
		try {
			return adao.readAuthorByID(authorId);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getAuthorsCount() {
		try {
			return adao.readAllAuthors(null).size();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public Integer getBooksCount() {
		try {
			return bdao.readAllBooks(null).size();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getGenresCount() {
		try {
			return gdao.readAllGenres(null).size();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getPublishersCount() {
		try {
			return pdao.readAllPublishers(null).size();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getBorrowersCount() {
		try {
			return brdao.readAllBorrowers(null).size();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getBranchesCount() {
		try {
			return lbdao.readAllBranches(null).size();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	public void deleteGenre(Genre genre) {
		try {
			gdao.deleteGenre(genre);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void deletePublisher(Publisher publisher) {
		try {
			pdao.deletePublisher(publisher);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void deleteBorrower(Borrower borrower) {
		try {
			brdao.deleteBorrower(borrower);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void deleteBranch(LibraryBranch branch) {
		try {
			lbdao.deleteBranch(branch);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param pageNo
	 * @param searchString
	 * @return
	 */
	public List<Author> getAuthorsByName(Integer pageNoThenCount, String authorName) {
		try {
			List<Author> authList = adao.readAuthorsByName(pageNoThenCount, authorName);
			return authList;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param pageNo
	 * @param searchString
	 * @return
	 */
	public List<Book> getBooksByName(Integer pageNoThenCount, String bookName) {
		try {
			List<Book> bookList = bdao.readBooksByName(pageNoThenCount, bookName);
			return bookList;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * @param pageNo
	 * @param searchString
	 * @return
	 */
	public List<Genre> getGenresByName(Integer pageNoThenCount, String genreName) {
		try {
			List<Genre> genreList = gdao.readGenresByName(pageNoThenCount, genreName);
			return genreList;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * @param pageNo
	 * @param searchString
	 * @return
	 */
	public List<Publisher> getPublishersByName(Integer pageNoThenCount, String publisherName) {
		try {
			List<Publisher> publisherList = pdao.readPublishersByName(pageNoThenCount, publisherName);
			return publisherList;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * @param pageNo
	 * @param searchString
	 * @return
	 */
	public List<Borrower> getBorrowersByName(Integer pageNoThenCount, String borrowerName) {
		try {
			List<Borrower> borrowerList = brdao.readBorrowersByName(pageNoThenCount, borrowerName);
			return borrowerList;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * @param pageNo
	 * @param searchString
	 * @return
	 */
	public List<LibraryBranch> getBranchesByName(Integer pageNoThenCount, String branchName) {
		try {
			List<LibraryBranch> branchList = lbdao.readBranchesByName(pageNoThenCount, branchName);
			return branchList;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getBranchSearchCount(String branchName) {
		try {
			List<LibraryBranch> branchList = lbdao.readBranchesByName(null, branchName);
			return branchList.size();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getAuthorSearchCount(String authorName) {
		try {
			List<Author> authList = adao.readAuthorsByName(null, authorName);
			return authList.size();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
