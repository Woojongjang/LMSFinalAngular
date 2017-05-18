package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Genre;
/**
 * This is a DAO
 * @author ppradhan
 *
 */
public class AuthorDAO extends BaseDAO implements ResultSetExtractor<List<Author>>{
	
	public void addAuthor(Author author) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_author (authorName) values (?)", new Object[] {author.getAuthorName()});
	}
	
	public void updateAuthor(Author author) throws ClassNotFoundException, SQLException{
		template.update("update tbl_author set authorName = ? where authorId = ?", new Object[]{author.getAuthorName(), author.getAuthorId()});
	}
	
	public void deleteAuthor(Author author) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_author where authorId = ?", new Object[] {author.getAuthorId()});
	}
	
	public List<Author> readAllAuthors(Integer pageNo) throws ClassNotFoundException, SQLException{
		String query = "select * from tbl_author";
		if(pageNo != null) {
			setPageSize(10);
			setPageNo(pageNo);
			setLimit(query);
		}
		return template.query(query, this);
	}
	
	public Author readAuthorByID(Integer authorID) throws ClassNotFoundException, SQLException{
		List<Author> authors = template.query("select * from tbl_author where authorId = ?", new Object[]{authorID}, this);
		if(authors!=null && !authors.isEmpty()){
			return authors.get(0);
		}
		return null;
	}
	
	public List<Author> readAuthorsByName(String  authorName) throws ClassNotFoundException, SQLException{
		setPageSize(10);
		authorName = "%"+authorName+"%";
		return template.query("select * from tbl_author where authorName like ?", new Object[]{authorName}, this);
	}
	
	public List<Author> readAuthorsByName(Integer pageNo, String  authorName) throws ClassNotFoundException, SQLException{
		if(pageNo!=null) {
			setPageSize(10);
			setPageNo(pageNo);
		}
		authorName = "%"+authorName+"%";
		List<Author> retList = template.query("select * from tbl_author where authorName like ?", new Object[]{authorName}, this);
		return retList;
	}

	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException {
		List<Author> authors = new ArrayList<>();
		while(rs.next()){
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			authors.add(a);
		}
		return authors;
	}
}
