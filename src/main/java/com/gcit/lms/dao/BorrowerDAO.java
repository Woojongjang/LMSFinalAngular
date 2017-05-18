package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Borrower;
/**
 * This is a DAO
 * @borrower woojong
 *
 */
public class BorrowerDAO extends BaseDAO implements ResultSetExtractor<List<Borrower>>{
	
	public void addBorrower(Borrower borrower) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_borrower (name, address, phone) values (?,?,?)",
				new Object[] {borrower.getBorrowerName(),borrower.getBorrowerAddress(),borrower.getBorrowerPhone()});
	}
	
	public void updateBorrower(Borrower borrower) throws ClassNotFoundException, SQLException{
		template.update("update tbl_borrower set name = ?, address = ?, phone = ? where cardNo = ?",
				new Object[]{borrower.getBorrowerName(), borrower.getBorrowerAddress(),borrower.getBorrowerPhone(), borrower.getBorrowerId()});
	}
	
	public void deleteBorrower(Borrower borrower) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_borrower where cardNo = ?", new Object[] {borrower.getBorrowerId()});
	}
	
	public List<Borrower> readAllBorrowers(Integer pageNo) throws ClassNotFoundException, SQLException{
		if(pageNo!=null) {
			setPageNo(pageNo);
			setPageSize(10);
		}
		return template.query("select * from tbl_borrower", new Object[]{}, this);
	}
	
	public Borrower readBorrowerByID(Integer borrowerID) throws ClassNotFoundException, SQLException{
		List<Borrower> borrowers = template.query("select * from tbl_borrower where cardNo = ?", new Object[]{borrowerID}, this);
		if(borrowers!=null && !borrowers.isEmpty()){
			return borrowers.get(0);
		}
		return null;
	}
	
	public List<Borrower> readBorrowersByName(String name) throws ClassNotFoundException, SQLException{
		setPageSize(10);
		name = "%"+name+"%";
		return template.query("select * from tbl_borrower where name like ?", new Object[]{name}, this);
	}
	
	public List<Borrower> readBorrowersByName(Integer pageNo, String name) throws ClassNotFoundException, SQLException{
		if(pageNo!=null) {
			setPageNo(pageNo);
			setPageSize(10);
		}
		name = "%"+name+"%";
		List<Borrower> retList = template.query("select * from tbl_borrower where name like ?", new Object[]{name}, this);
		return retList;
	}
	
//	public Integer getBorrowersCount() throws ClassNotFoundException, SQLException{
//		return readCount("select count(*) as COUNT from tbl_borrower", null);
//	}

	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		//BookDAO bdao = new BookDAO(conn);
		List<Borrower> borrowers = new ArrayList<>();
		while(rs.next()){
			Borrower p = new Borrower();
			p.setBorrowerId(rs.getInt("cardNo"));
			String name = rs.getString("name");
			String addr = rs.getString("address");
			String phone = rs.getString("phone");
			if(name == null){
				p.setBorrowerName("NO BORROWER NAME");
			}
			else if(name.equals("")){
				p.setBorrowerName("NO BORROWER NAME");
			}
			else {
				p.setBorrowerName(name);
			}
			if(addr == null){
				p.setBorrowerAddress("NO BORROWER ADDRESS");
			}
			else if(addr.equals("")){
				p.setBorrowerAddress("NO BORROWER ADDRESS");
			}
			else {
				p.setBorrowerAddress(addr);
			}
			if(phone == null){
				p.setBorrowerPhone("NO BORROWER PHONE");
			}
			else if(phone.equals("")){
				p.setBorrowerPhone("NO BORROWER PHONE");
			}
			else {
				p.setBorrowerPhone(phone);
			}
			//p.setBooks(books);
			borrowers.add(p);
		}
		return borrowers;
	}
}
