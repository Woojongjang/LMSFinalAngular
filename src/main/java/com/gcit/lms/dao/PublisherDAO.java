package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Publisher;
/**
 * This is a DAO
 * @publisher woojong
 *
 */
public class PublisherDAO extends BaseDAO implements ResultSetExtractor<List<Publisher>>{
	
	public void addPublisher(Publisher publisher) throws ClassNotFoundException, SQLException{
		template.update("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?,?,?)",
				new Object[] {publisher.getPublisherName(),publisher.getPublisherAddress(),publisher.getPublisherPhone()});
	}
	
	public void updatePublisher(Publisher publisher) throws ClassNotFoundException, SQLException{
		template.update("update tbl_publisher set publisherName = ?, publisherAddress = ?, publisherPhone = ? where publisherId = ?",
				new Object[]{publisher.getPublisherName(), publisher.getPublisherAddress(),publisher.getPublisherPhone(), publisher.getPublisherId()});
	}
	
	public void deletePublisher(Publisher publisher) throws ClassNotFoundException, SQLException{
		template.update("delete from tbl_publisher where publisherId = ?", new Object[] {publisher.getPublisherId()});
	}
	
	public List<Publisher> readAllPublishers(Integer pageNo) throws ClassNotFoundException, SQLException{
		if(pageNo!=null) {
			setPageNo(pageNo);
			setPageSize(10);
		}
		return template.query("select * from tbl_publisher", new Object[]{}, this);
	}
	
	public Publisher readPublisherByID(Integer publisherID) throws ClassNotFoundException, SQLException{
		List<Publisher> publishers = template.query("select * from tbl_publisher where publisherId = ?", new Object[]{publisherID}, this);
		if(publishers!=null && !publishers.isEmpty()){
			return publishers.get(0);
		}
		return null;
	}
	
	public List<Publisher> readPublishersByName(String  publisherName) throws ClassNotFoundException, SQLException{
		setPageSize(10);
		publisherName = "%"+publisherName+"%";
		return template.query("select * from tbl_publisher where publisherName like ?", new Object[]{publisherName}, this);
	}
	
	public List<Publisher> readPublishersByName(Integer pageNo, String  publisherName) throws ClassNotFoundException, SQLException{
		if(pageNo!=null) {
			setPageNo(pageNo);
			setPageSize(10);
		}
		publisherName = "%"+publisherName+"%";
		List<Publisher> retList = template.query("select * from tbl_publisher where publisherName like ?", new Object[]{publisherName}, this);
		return retList;
	}
	
//	public Integer getPublishersCount() throws ClassNotFoundException, SQLException{
//		return readCount("select count(*) as COUNT from tbl_publisher", null);
//	}

	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		//BookDAO bdao = new BookDAO(conn);
		List<Publisher> publishers = new ArrayList<>();
		while(rs.next()){
			Publisher p = new Publisher();
			p.setPublisherId(rs.getInt("publisherId"));
			String name = rs.getString("publisherName");
			String addr = rs.getString("publisherAddress");
			String phone = rs.getString("publisherPhone");
			if(name == null){
				p.setPublisherName("NO PUBLISHER NAME");
			}
			else if(name.equals("")){
				p.setPublisherName("NO PUBLISHER NAME");
			}
			else {
				p.setPublisherName(name);
			}
			if(addr == null){
				p.setPublisherAddress("NO PUBLISHER ADDRESS");
			}
			else if(addr.equals("")){
				p.setPublisherAddress("NO PUBLISHER ADDRESS");
			}
			else {
				p.setPublisherAddress(addr);
			}
			if(phone == null){
				p.setPublisherPhone("NO PUBLISHER PHONE");
			}
			else if(phone.equals("")){
				p.setPublisherPhone("NO PUBLISHER PHONE");
			}
			else {
				p.setPublisherPhone(phone);
			}
			//a.setBooks(bdao.readFirstLevel("select * from tbl_book where bookId IN (Select bookId from tbl_book_publishers where publisherId = ?)", new Object[]{a.getPublisherId()}));
			publishers.add(p);
		}
		return publishers;
	}
}
