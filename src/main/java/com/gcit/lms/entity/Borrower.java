package com.gcit.lms.entity;

import java.io.Serializable;
import java.util.List;

public class Borrower implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1166092053576845398L;
	private Integer borrowerId;
	private String borrowerName;
	private String borrowerAddress;
	private String borrowerPhone;
	private List<BookLoan> books;
	/**
	 * @return the borrowerId
	 */
	public Integer getBorrowerId() {
		return borrowerId;
	}
	/**
	 * @param borrowerId the borrowerId to set
	 */
	public void setBorrowerId(Integer borrowerId) {
		this.borrowerId = borrowerId;
	}
	/**
	 * @return the borrowerName
	 */
	public String getBorrowerName() {
		return borrowerName;
	}
	/**
	 * @param borrowerName the borrowerName to set
	 */
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}
	/**
	 * @return the borrowerAddress
	 */
	public String getBorrowerAddress() {
		return borrowerAddress;
	}
	/**
	 * @param borrowerAddress the borrowerAddress to set
	 */
	public void setBorrowerAddress(String borrowerAddress) {
		this.borrowerAddress = borrowerAddress;
	}
	/**
	 * @return the borrowerPhone
	 */
	public String getBorrowerPhone() {
		return borrowerPhone;
	}
	/**
	 * @param borrowerPhone the borrowerPhone to set
	 */
	public void setBorrowerPhone(String borrowerPhone) {
		this.borrowerPhone = borrowerPhone;
	}
	/**
	 * @return the books
	 */
	public List<BookLoan> getBooks() {
		return books;
	}
	/**
	 * @param books the books to set
	 */
	public void setBooks(List<BookLoan> books) {
		this.books = books;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((books == null) ? 0 : books.hashCode());
		result = prime * result + ((borrowerAddress == null) ? 0 : borrowerAddress.hashCode());
		result = prime * result + ((borrowerId == null) ? 0 : borrowerId.hashCode());
		result = prime * result + ((borrowerName == null) ? 0 : borrowerName.hashCode());
		result = prime * result + ((borrowerPhone == null) ? 0 : borrowerPhone.hashCode());
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
		Borrower other = (Borrower) obj;
		if (books == null) {
			if (other.books != null)
				return false;
		} else if (!books.equals(other.books))
			return false;
		if (borrowerAddress == null) {
			if (other.borrowerAddress != null)
				return false;
		} else if (!borrowerAddress.equals(other.borrowerAddress))
			return false;
		if (borrowerId == null) {
			if (other.borrowerId != null)
				return false;
		} else if (!borrowerId.equals(other.borrowerId))
			return false;
		if (borrowerName == null) {
			if (other.borrowerName != null)
				return false;
		} else if (!borrowerName.equals(other.borrowerName))
			return false;
		if (borrowerPhone == null) {
			if (other.borrowerPhone != null)
				return false;
		} else if (!borrowerPhone.equals(other.borrowerPhone))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Borrower [borrowerId=" + borrowerId + ", borrowerName=" + borrowerName + ", borrowerAddress="
				+ borrowerAddress + ", borrowerPhone=" + borrowerPhone + ", books=" + books + "]";
	}
	
	
}
