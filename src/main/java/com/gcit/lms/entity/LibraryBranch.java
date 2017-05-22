package com.gcit.lms.entity;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.JSONPObject;

//class BookKeyDeserializer extends KeyDeserializer
//{
//    @Override
//    public Object deserializeKey( String key, DeserializationContext ctxt )
//    		throws IOException, JsonProcessingException {
////        Book newBook = new Book();
////        newBook.setBookId(parseBookId( key ));
////        newBook.setTitle(parseTitle( key ));
//    	ObjectMapper mapper = new ObjectMapper();
//    	Book newBook = mapper.readValue(key, Book.class);
//        return newBook;
//    }
//}

public class LibraryBranch implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5485198573104394925L;
	private Integer branchId;
	private String branchName;
	private String branchAddress;
	
	private List<Book> books;
	private HashMap<Integer, Integer> booksCount;
	
//	@JsonDeserialize( keyUsing = BookKeyDeserializer.class )
//	private HashMap<Book, Integer> booksKeyCount;
	
	/**
	 * @return the branchId
	 */
	public Integer getBranchId() {
		return branchId;
	}
	/**
	 * @param branchId the branchId to set
	 */
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}
	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	/**
	 * @return the branchAddress
	 */
	public String getBranchAddress() {
		return branchAddress;
	}
	/**
	 * @param branchAddress the branchAddress to set
	 */
	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}
	
	
	/**
	 * @return the books
	 */
	public List<Book> getBooks() {
		return books;
	}
	/**
	 * @param books the books to set
	 */
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	/**
	 * @return the booksCount
	 */
	public HashMap<Integer, Integer> getBooksCount() {
		return booksCount;
	}
	/**
	 * @param booksCount the booksCount to set
	 */
	public void setBooksCount(HashMap<Integer, Integer> booksCount) {
		this.booksCount = booksCount;
	}
	/**
	 * @return the booksKeyCount
	 */
//	public HashMap<Book, Integer> getBooksKeyCount() {
//		return booksKeyCount;
//	}
//	/**
//	 * @param booksKeyCount the booksKeyCount to set
//	 */
//	public void setBooksKeyCount(HashMap<Book, Integer> booksKeyCount) {
//		this.booksKeyCount = booksKeyCount;
//	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((branchAddress == null) ? 0 : branchAddress.hashCode());
		result = prime * result + ((branchId == null) ? 0 : branchId.hashCode());
		result = prime * result + ((branchName == null) ? 0 : branchName.hashCode());
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
		LibraryBranch other = (LibraryBranch) obj;
		if (branchAddress == null) {
			if (other.branchAddress != null)
				return false;
		} else if (!branchAddress.equals(other.branchAddress))
			return false;
		if (branchId == null) {
			if (other.branchId != null)
				return false;
		} else if (!branchId.equals(other.branchId))
			return false;
		if (branchName == null) {
			if (other.branchName != null)
				return false;
		} else if (!branchName.equals(other.branchName))
			return false;
		return true;
	}
	
}
