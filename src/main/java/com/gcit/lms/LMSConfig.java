/**
 * LMSConfig.java
 * @author Woo-Jong Jang <jangwoojong@gmail.com>
 * Created on May 7, 2017
 */
package com.gcit.lms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.service.AdminService;
import com.gcit.lms.service.BookLoanService;
import com.gcit.lms.service.BorrowerService;
import com.gcit.lms.service.LibrarianService;

@Configuration
public class LMSConfig {
	public String driver = "com.mysql.jdbc.Driver";
	public String url = "jdbc:mysql://localhost/library";
	public String username = "root";
	public String password = "cseGCIT2017";
	
	@Bean
	public BasicDataSource dataSource(){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}
	
	@Bean
	public JdbcTemplate template() {
		return new JdbcTemplate(dataSource());
	}
	
	@Bean
	public PlatformTransactionManager txManager(){
		DataSourceTransactionManager dsManager = new DataSourceTransactionManager();
		dsManager.setDataSource(dataSource());
		return dsManager;
	}
	
	@Bean
	public AuthorDAO adao(){
		return new AuthorDAO();
	}
	
	@Bean
	public BookDAO bdao(){
		return new BookDAO();
	}
	
	@Bean
	public PublisherDAO pdao(){
		return new PublisherDAO();
	}
	
	@Bean
	public GenreDAO gdao(){
		return new GenreDAO();
	}
	
	@Bean
	public LibraryBranchDAO lbdao(){
		return new LibraryBranchDAO();
	}
	
	@Bean
	public BorrowerDAO brdao(){
		return new BorrowerDAO();
	}
	
	@Bean
	public BookLoanDAO bldao(){
		return new BookLoanDAO();
	}
	
	@Bean
	public AdminService adminService(){
		return new AdminService();
	}
	
	@Bean
	public BookLoanService bookLoanService(){
		return new BookLoanService();
	}
	
	@Bean
	public BorrowerService borrowerService(){
		return new BorrowerService();
	}
	
	@Bean
	public LibrarianService librarianService(){
		return new LibrarianService();
	}
}
