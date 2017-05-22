package com.gcit.lms;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.service.AdminService;

/**
 * Handles requests for the application home page.
 */
@RestController
public class HomeController {

	@Autowired
	AuthorDAO adao;

	@Autowired
	BookDAO bdao;

	@Autowired
	AdminService adminService;

//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String home(Locale locale, Model model) {
//		return "welcome";
//	}
//
//	@RequestMapping(value = "/admin", method = RequestMethod.GET)
//	public String admin(Locale locale, Model model) {
//		return "admin";
//	}
//
//	@RequestMapping(value = "/author", method = RequestMethod.GET)
//	public String author(Locale locale, Model model) {
//		return "author";
//	}
//
//	@RequestMapping(value = "/addAuthor", method = RequestMethod.GET)
//	public String addAuthor(Locale locale, Model model) {
//		return "addauthor";
//	}

	@RequestMapping(value = "/viewSimpleAuthors", method = RequestMethod.GET)
	public String viewAuthors(Locale locale, Model model) {
		List<Author> authors = new ArrayList<Author>();
		try {
			authors = adao.readAllAuthors(1);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		model.addAttribute("authors", authors);
		return "viewauthors";
	}
	
	@RequestMapping(value = "/viewAuthors/{pageNo}", method = RequestMethod.GET, produces = "application/json")
	public List<Author> viewTestParam(@PathVariable Integer pageNo,
			@RequestParam(value = "searchString", required = false) String searchString) {
		List<Author> authors = new ArrayList<>();
		try {
			if(searchString!=null && searchString.length()>0) {
				Author temp = new Author();
				temp.setAuthorName(searchString);
				authors.add(temp);
				return authors;
			}
			authors = adao.readAllAuthors(pageNo);
			for (Author a : authors) {
				a.setBooks(bdao.readAllBooksByAuthorID(a.getAuthorId()));
			}
			return authors;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	//REMOVE AND ADD WEB SERVICE TO JAVA SERVICE
	@RequestMapping(value = "/viewAuthorsServ/{pageNo}/{searchString}", method = RequestMethod.GET, produces = "application/json")
	public List<Author> viewAuthorsServ(@PathVariable Integer pageNo, @PathVariable String searchString) {
		List<Author> authors = new ArrayList<>();
		try {
			if (searchString != null && searchString.length() > 0) {
				authors = adao.readAuthorsByName(searchString);
			} else {
				authors = adao.readAllAuthors(pageNo);
			}

			for (Author a : authors) {
				a.setBooks(bdao.readAllBooksByAuthorID(a.getAuthorId()));
			}
			return authors;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/updateAuthorServ", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public List<Author> updateAuthorServ(@RequestBody Author author) {
		try {
			adao.updateAuthor(author);
			return adao.readAllAuthors(1);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// WEB SERVICE TEST
	@RequestMapping(value = "/viewSimpleAuthors", method = RequestMethod.GET, produces="application/json")
	public List<Author> viewSimpleAuthors(Locale locale, Model model) {
		List<Author> authors = new ArrayList<>();
			try {
				authors = adao.readAllAuthors(1);
				for(Author a: authors) {
					a.setBooks(bdao.readAllBooksByAuthorID(a.getAuthorId()));
				}
				return authors;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	@RequestMapping(value = "/addAuthorServ", method = RequestMethod.POST, consumes="application/json")
	public String addAuthorServ(@RequestBody Author author) {
		try {
			adao.addAuthor(author);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "AUTHOR ADDED";
	}
	
	@RequestMapping(value = "/addBookAuthorServ", method = RequestMethod.POST, consumes="application/json")
	public String addBookAuthorServ(@RequestBody Book book) {
		try {
			List<Author> bookAuthors = book.getAuthors();
//			bdao.addBookAuthors(book, book.getAuthors());
			Integer bookId = book.getBookId();
			if(bookAuthors!=null && !bookAuthors.isEmpty()) {
//				int count = 0;
				for(Author bAuthElem : bookAuthors) {
//					count += 1;
//					System.out.println(count);
					Integer authId = bAuthElem.getAuthorId();
					bdao.addBookAuthorInteger(bookId,authId);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "BOOK AUTHOR ADDED";
	}

}
