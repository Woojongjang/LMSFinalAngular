package com.gcit.lms.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.gcit.lms.dao.BookBranchCountDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoan;
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
	
	@Autowired
	BookBranchCountDAO bbcdao;
	
	@Autowired
	BookLoanDAO bldao;
	
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
	@RequestMapping(value = "/Books", method = RequestMethod.POST, consumes="application/json", produces = "text/plain")
	public String addBook(@RequestBody Book book) {
		try {
			// EDIT BookDAO for addBookWithID to work with returning generated key
			Integer bookId = bdao.addBookWithID(book);
			book.setBookId(bookId);
			List<Author> bookAuthors = book.getAuthors();
			List<Genre> bookGenres = book.getGenres();
			Publisher bookPublisher = book.getPublisher();
			if(bookAuthors!=null && !bookAuthors.isEmpty()) {
				for(Author bAuthElem : bookAuthors) {
					bdao.addBookAuthors(book,bAuthElem);
				}
			}
			if(bookGenres!=null && !bookGenres.isEmpty()) {
				for(Genre bGenreElem : bookGenres) {
					bdao.addBookGenres(bookId,bGenreElem.getGenreId());
				}
			}
			if(bookPublisher!=null) {
				bdao.addBookPublisher(bookId,bookPublisher.getPublisherId());
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "ERROR BOOK NOT ADDED";
		}
		return "BOOK ADDED";
	}
	
	@RequestMapping(value="/Books", method = RequestMethod.GET, produces = "application/json")
	public List<Book> getAllBooks(@RequestParam(value="pageNo",required=false) Integer pageNo, 
			@RequestParam(value = "searchString", required = false) String searchString) {
		List<Book> books = new ArrayList<Book>();
		try {
			if (searchString!=null && searchString.length()>0) {
				books = bdao.readBooksByName(pageNo, searchString);
			} else if (pageNo!=null&&pageNo>0) {
				books = bdao.readAllBooks(pageNo);
			}
			else {
				books = bdao.readAllBooks(null);
			}
			
			for (Book b : books) {
				b.setAuthors(adao.readAuthorByBookID(b.getBookId()));
				b.setGenres(gdao.readGenreByBookID(b.getBookId()));
				b.setPublisher(pdao.readPublisherByBookID(b.getBookId()));
			}
			return books;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/Books/{bookId}", method = RequestMethod.GET, produces = "application/json")
	public Book getBookById(@PathVariable Integer bookId) {
		try {
			Book book = bdao.readBookByID(bookId);
			book.setAuthors(adao.readAuthorByBookID(bookId));
			book.setGenres(gdao.readGenreByBookID(bookId));
			book.setPublisher(pdao.readPublisherByBookID(bookId));
			return book;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	@RequestMapping(value = {"/Books", "/Books/{bookId}"}, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Book editCreateBook(@RequestBody Book book, @PathVariable Optional<Integer> bookId) {
		try {
			if(bookId.isPresent()) {
				book.setBookId(bookId.get());
			}
			bdao.updateOrCreateBook(book);
			return bdao.readBookByID(book.getBookId());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	@RequestMapping(value = "/Books/{bookId}", method = RequestMethod.DELETE, produces="text/plain")
	public String deleteBook(@PathVariable Integer bookId) {
		try {
			Book book = new Book();
			book.setBookId(bookId);
			bdao.deleteBook(book);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "BOOK DELETED";
	}
	
	//-------------GENRE SERVICES-------------//
	
	@RequestMapping(value = "/initGenre", method = RequestMethod.GET, produces="application/json")
	public Genre initGenre() {
		return new Genre();
	}
	
	@Transactional
	@RequestMapping(value = "/Genres", method = RequestMethod.POST, consumes="application/json", produces="text/plain")
	public String addGenre(@RequestBody Genre genre)  {
		try {
			gdao.addGenre(genre);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "GENRE ADDED";
	}
	
	@RequestMapping(value="/Genres", method=RequestMethod.GET, produces="application/json")
	public List<Genre> getAllGenres(@RequestParam(value="pageNo",required=false) Integer pageNo, 
			@RequestParam(value = "searchString", required = false) String searchString) {
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
	
	@Transactional
	@RequestMapping(value = "/Genres", method = RequestMethod.PUT, consumes = "application/json", produces = "text/plain")
	public String editGenre(@RequestBody Genre genre) {
		try {
			gdao.updateGenre(genre);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "GENRE UPDATED";
	}

	@Transactional
	@RequestMapping(value = "/Genres/{genreId}", method = RequestMethod.DELETE, produces="text/plain")
	public String deleteGenre(@PathVariable Integer genreId) {
		try {
			Genre genre = new Genre();
			genre.setGenreId(genreId);
			gdao.deleteGenre(genre);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "GENRE DELETED";
	}
	
	//-------------PUBLISHER SERVICES-------------//
	
	@RequestMapping(value = "/initPublisher", method = RequestMethod.GET, produces="application/json")
	public Publisher initPublisher() {
		return new Publisher();
	}
	
	@Transactional
	@RequestMapping(value = "/Publishers", method = RequestMethod.POST, consumes="application/json", produces="text/plain")
	public String addPublisher(@RequestBody Publisher publisher) {
		try {
			pdao.addPublisher(publisher);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "PUBLISHER ADDED";
	}
	
	@RequestMapping(value = "/Publishers", method=RequestMethod.GET, produces="application/json")
	public List<Publisher> getAllPublishers(@RequestParam(value="pageNo",required=false) Integer pageNo, 
			@RequestParam(value = "searchString", required = false) String searchString) {
		List<Publisher> publishers = new ArrayList<>();
		try {
			publishers = pdao.readAllPublishers(null);
			return publishers;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	@RequestMapping(value = "/Publishers", method = RequestMethod.PUT, consumes = "application/json", produces = "text/plain")
	public String editPublisher(@RequestBody Publisher publisher) {
		try {
			pdao.updatePublisher(publisher);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "PUBLISHER UPDATED";
	}
	
	@Transactional
	@RequestMapping(value = "/Publishers/{publisherId}", method = RequestMethod.DELETE, produces="text/plain")
	public String deletePublisher(@PathVariable Integer publisherId) {
		try {
			Publisher publisher = new Publisher();
			publisher.setPublisherId(publisherId);
			pdao.deletePublisher(publisher);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "PUBLISHER DELETED";
	}
	
	//-------------BORROWER SERVICES-------------//
	
	@RequestMapping(value = "/initBorrower", method = RequestMethod.GET, produces="application/json")
	public Borrower initBorrower() {
		return new Borrower();
	}
	
	@Transactional
	@RequestMapping(value = "/Borrowers", method = RequestMethod.POST, consumes="application/json", produces="text/plain")
	public String addBorrower(@RequestBody Borrower borrower) {
		try {
			brdao.addBorrower(borrower);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "BORROWER ADDED";
	}
	
	@RequestMapping(value="/Borrowers", method=RequestMethod.GET, produces="application/json")
	public List<Borrower> getAllBorrowers() {
		try {
			return brdao.readAllBorrowers(null);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	@RequestMapping(value = "/Borrowers", method = RequestMethod.PUT, consumes = "application/json", produces = "text/plain")
	public String editBorrower(@RequestBody Borrower borrower) {
		try {
			brdao.updateBorrower(borrower);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "BORROWER UPDATED";
	}
	
	@Transactional
	@RequestMapping(value = "/Borrowers/{borrowerId}", method = RequestMethod.DELETE, produces="text/plain")
	public String deleteBorrower(@PathVariable Integer borrowerId) {
		try {
			Borrower borrower = new Borrower();
			borrower.setBorrowerId(borrowerId);
			brdao.deleteBorrower(borrower);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "BORROWER DELETED";
	}
	
	//-------------LIBRARY SERVICES-------------//
	
	@RequestMapping(value = "/initLibrary", method = RequestMethod.GET, produces="application/json")
	public LibraryBranch initLibrary() {
		return new LibraryBranch();
	}
	
	@Transactional
	@RequestMapping(value = "/Libraries", method = RequestMethod.POST, consumes="application/json", produces="text/plain")
	public String addBranch(@RequestBody LibraryBranch branch) {
		try {
			lbdao.addBranch(branch);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "LIBRARY ADDED";
	}
	
	@RequestMapping(value = "/Libraries", method = RequestMethod.GET, produces = "application/json")
	public List<LibraryBranch> getAllLibraries(@RequestParam(value="pageNo",required=false) Integer pageNo, 
			@RequestParam(value = "searchString", required = false) String searchString) {
		List<LibraryBranch> branches = new ArrayList<>();
		try {
			if (searchString!=null && searchString.length()>0) {
				branches = lbdao.readBranchesByName(pageNo, searchString);
			} else 
			if (pageNo!=null&&pageNo>0) {
				branches = lbdao.readAllBranches(pageNo);
			}
			else {
				branches = lbdao.readAllBranches(null);
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return branches;
	}
	
	@Transactional
	@RequestMapping(value = "/Libraries", method = RequestMethod.PUT, consumes = "application/json", produces = "text/plain")
	public String editBranch(@RequestBody LibraryBranch branch) {
		try {
			lbdao.updateBranch(branch);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "LIBRARY UPDATED";
	}
	
	@Transactional
	@RequestMapping(value = "/Libraries/{branchId}", method = RequestMethod.DELETE, produces="text/plain")
	public String deleteLibrary(@PathVariable Integer branchId) {
		try {
			LibraryBranch library = new LibraryBranch();
			library.setBranchId(branchId);
			lbdao.deleteBranch(library);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "LIBRARY DELETED";
	}
	
	
	//-------------BOOKLOAN SERVICES-------------//
	
	@RequestMapping(value = "/Borrower/BookLoans", method = RequestMethod.GET, produces = "application/json")
	public List<BookLoan> getAllBookLoans() {
		List<BookLoan> bookLoans = new ArrayList<BookLoan>();
		try {
			bookLoans = bldao.readAllBookLoans(null);
			for(BookLoan loans : bookLoans) {
				loans.setBook(bdao.readBookByID(loans.getBook().getBookId()));
				loans.setBorrower(brdao.readBorrowerByID(loans.getBorrower().getBorrowerId()));
				loans.setBranch(lbdao.readBranchByID(loans.getBranch().getBranchId()));
			}
			return bookLoans;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	@RequestMapping(value = "/Borrower/BookLoans", method = RequestMethod.PUT, consumes="application/json", produces="text/plain")
	public String updateBookLoan(@RequestBody BookLoan loan) {
		try {
			bldao.updateBookLoan(loan);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return "BOOK LOAN UPDATED";
	}
	
	@Transactional
	@RequestMapping(value = "/Borrower/BookLoans", method = RequestMethod.DELETE, produces="text/plain")
	public String deleteBookLoan(@RequestParam(value = "bookId", required = true) Integer bookId,
			@RequestParam(value = "branchId", required = true) Integer branchId,
			@RequestParam(value = "cardNo", required = true) Integer cardNo,
			@RequestParam(value = "dateOut", required = true) String dateOut) {
		try {
			String decodedDate = URLDecoder.decode(dateOut, "UTF-8");
			bldao.deleteBookLoanByID(bookId, branchId, cardNo, decodedDate);
		} catch (ClassNotFoundException | SQLException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "BOOK LOAN DELETED";
	}
}
