package com.gcit.lms.entity;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable{
	
	private static final long serialVersionUID = -1070838265691816263L;

	private Integer bookId;
	private String title;
	private List<Author> authors;
	private Publisher publisher;
	public List<Genre> genres;
	
	/**
	 * @return the bookId
	 */
	public Integer getBookId() {
		return bookId;
	}
	/**
	 * @param bookId the bookId to set
	 */
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the authors
	 */
	public List<Author> getAuthors() {
		return authors;
	}
	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	/**
	 * @return the authors String version
	 */
	public String getAuthorString() {
		String authorString = "";
		if(this.authors!=null && !this.authors.isEmpty()) {
			int count = 1;
			for(Author a : this.authors) {
				if(count == 1) {
					authorString = authorString.concat(a.getAuthorName());
				}
				else {
					authorString = authorString.concat(", "+a.getAuthorName());
				}
				count++;
			}
		}
		return authorString;
	}
	/**
	 * @return the publisher
	 */
	public Publisher getPublisher() {
		return publisher;
	}
	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	/**
	 * @return the genres String version
	 */
	public String getGenreString() {
		String genreString = "";
		if(this.genres!=null && !this.genres.isEmpty()) {
			int count = 1;
			for(Genre g : this.genres) {
				if(count == 1) {
					genreString = genreString.concat(g.getGenreName());
				}
				else {
					genreString = genreString.concat(", "+g.getGenreName());
				}
				count++;
			}
		}
		return genreString;
	}
	/**
	 * @param genres the genres to set
	 */
	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	
	public List<Genre> getGenres() {
		return genres;
	}
	
//	public String getDetails() {
//		String details = this.publisher.getPublisherName();
//		if(this.authors!=null && !this.authors.isEmpty()) {
//			for(Author a : this.authors) {
//				details = details.concat(", "+a.getAuthorName());
//			}
//		}
//		if(this.genres!=null && !this.genres.isEmpty()) {
//			for(Genre g : this.genres) {
//				details = details.concat(", "+g.getGenreName());
//			}
//		}
//		return details;
//	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (bookId == null) {
			if (other.bookId != null)
				return false;
		} else if (!bookId.equals(other.bookId))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
}
