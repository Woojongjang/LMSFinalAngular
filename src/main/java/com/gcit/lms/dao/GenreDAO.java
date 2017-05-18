package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Genre;
/**
 * This is a DAO
 * @genre woojong
 *
 */
public class GenreDAO extends BaseDAO implements ResultSetExtractor<List<Genre>>{
	
	public void addGenre(Genre genre) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_genre (genre_name) values (?)",
				new Object[] {genre.getGenreName()});
	}
	
	public void updateGenre(Genre genre) throws ClassNotFoundException, SQLException{
		template.update("update tbl_genre set genre_name = ? where genre_id = ?", new Object[]{genre.getGenreName(), genre.getGenreId()});
	}
	
	public void deleteGenre(Genre genre) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_genre where genre_id = ?", new Object[] {genre.getGenreId()});
	}
	
	public List<Genre> readAllGenres(Integer pageNo) throws ClassNotFoundException, SQLException{
		if(pageNo!=null) {
			setPageNo(pageNo);
			setPageSize(10);
		}
		return template.query("select * from tbl_genre", new Object[]{}, this);
	}
	
	public Genre readGenreByID(Integer genreId) throws ClassNotFoundException, SQLException{
		List<Genre> genres = template.query("select * from tbl_genre where genre_id = ?", new Object[]{genreId}, this);
		if(genres!=null && !genres.isEmpty()){
			return genres.get(0);
		}
		return null;
	}
	
	public List<Genre> readGenresByName(String  genreName) throws ClassNotFoundException, SQLException{
		setPageSize(10);
		genreName = "%"+genreName+"%";
		return template.query("select * from tbl_genre where genre_name like ?", new Object[]{genreName}, this);
	}
	
	public List<Genre> readGenresByName(Integer pageNo, String  genreName) throws ClassNotFoundException, SQLException{
		if(pageNo!=null) {
			setPageNo(pageNo);
			setPageSize(10);
		}
		genreName = "%"+genreName+"%";
		List<Genre> retList = template.query("select * from tbl_genre where genre_name like ?", new Object[]{genreName}, this);
		return retList;
	}
	
//	public Integer getGenresCount() throws ClassNotFoundException, SQLException{
//		return readCount("select count(*) as COUNT from tbl_genre", new Object[]{}, this);
//	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException {
		//BookDAO bdao = new BookDAO(conn);
		List<Genre> genres = new ArrayList<>();
		while(rs.next()){
			Genre p = new Genre();
			p.setGenreId(rs.getInt("genre_id"));
			String genreName = rs.getString("genre_name");
			if(genreName != null) {
				p.setGenreName(genreName);
			}
			else {
				p.setGenreName("NO GENRE");
			}
			//a.setBooks(bdao.readFirstLevel("select * from tbl_book where bookId IN (Select bookId from tbl_book_genres where genre_id = ?)", new Object[]{a.getGenreId()}));
			genres.add(p);
		}
		return genres;
	}
}
