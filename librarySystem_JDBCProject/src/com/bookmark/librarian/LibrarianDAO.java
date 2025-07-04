package com.bookmark.librarian;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookmark.common.DataSource;
import com.bookmark.vo.BookVO;
import com.bookmark.vo.CategoryVO;


public class LibrarianDAO {
	

	DataSource ds = new DataSource();
	
	
	// 마지막 최근 book id
	public int getLastBookId() {
		
		Connection con = null;
		
		try {
			con = ds.getConnection();
		
			String sql = "SELECT MAX(book_id) AS lastBookId "
				+ "FROM book";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				return rs.getInt("lastBookId");
			}
		
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}finally {
			ds.closeConnection(con);
		}
		return 0;
		
	}
	

	// 도서 등록 =
	public void insertBook(BookVO book) {
		Connection con = null;
		
		try {
			con = ds.getConnection();
			String sql = "INSERT INTO book VALUES (?, ?, ?, ?, ?, ?, ?)";
		
			PreparedStatement stmt = con.prepareStatement(sql);
			
			stmt.setInt(1, book.getBookId());     
			stmt.setInt(2, book.getCategoryId());  
			stmt.setString(3, book.getTitle());         
			stmt.setString(4, book.getAuthor());        
			stmt.setString(5, book.getPublisher());     
			stmt.setInt(6, book.getTotalCount()); 
			stmt.setDate(7, book.getCreateAt());             
			
			stmt.executeUpdate();
		
		}
		
		catch(SQLException e) {
			throw new RuntimeException(e);
		}finally {
			ds.closeConnection(con);
		}
		
	}
	
	
	
	/* 카테고리 조회 */
	public List<CategoryVO> getCategoryAll(){
		
		Connection con = null;
		List<CategoryVO> categoryList = new ArrayList<>();
		
		try {
			
			con = ds.getConnection();
			
			String selectCategoryAll = "SELECT "
					+ "category_id AS categoryId, "
					+ "name AS name "
					+ "FROM category";
			
			PreparedStatement stmt = con.prepareStatement(selectCategoryAll);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				
				categoryList.add(new CategoryVO(rs.getInt("categoryId"), rs.getString("name")));
				
			}
			
			return categoryList;
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}finally {
			ds.closeConnection(con);
		}
		
		
	}
	
	
	
	
	public List<BookVO> getBookAll(){
		
		Connection con = null;
		List<BookVO> bookList = new ArrayList<>();
		
		try {
			
			con = ds.getConnection();
			
			String selectBookAll = "SELECT"
					+ "  book_id AS bookId, "
					+ "  category_id AS categoryId, "
					+ "  title AS title, "
					+ "  author AS author, "
					+ "  publisher AS publisher, "
					+ "  total_count AS totalCount, "
					+ "  create_at AS createAt "
					+ "FROM "
					+ "  book "
					+ "JOIN category ON book.category_id = category.category_id";
			
			PreparedStatement stmt = con.prepareStatement(selectBookAll);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				bookList.add(new BookVO(rs.getInt("bookId")
							, rs.getInt("categoryId")
							, rs.getString("title")
							, rs.getString("author")
							, rs.getString("publisher")
							, rs.getInt("totalCount")
							, rs.getDate("createAt")));

			}
			
			
			return bookList;
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}finally {
			ds.closeConnection(con);
		}
	}

			

}
