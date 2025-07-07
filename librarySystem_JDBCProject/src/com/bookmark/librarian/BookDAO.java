package com.bookmark.librarian;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bookmark.common.DataSource;
import com.bookmark.vo.BookVO;
import com.bookmark.vo.BookWithCategoryVO;
import com.bookmark.vo.CategoryVO;


public class BookDAO {
	

	DataSource ds = new DataSource();
	Scanner scanner = new Scanner(System.in);
	
	
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
		return 1;
		
	}
	
	
	
	
	// 카테고리별 도서 목록
	public List<BookWithCategoryVO> getBooksByCategory(int categoryId) {
	    List<BookWithCategoryVO> bookList = new ArrayList<>();
	    Connection con = null;

	    String sql = "SELECT "
	               + "  book_id AS bookId, "
	               + "  category_id AS categoryId, "
	               + "  category_name AS categoryName, "
	               + "  title AS title, "
	               + "  author AS author, "
	               + "  publisher AS publisher, "
	               + "  total_count AS totalCount, "
	               + "  create_at AS createAt "
	               + "FROM book_with_category_view "
	               + "WHERE category_id = ?";

	    try {
	        con = ds.getConnection();
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setInt(1, categoryId);

	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            BookWithCategoryVO book = new BookWithCategoryVO(
	                rs.getInt("bookId"),
	                rs.getInt("categoryId"),
	                rs.getString("categoryName"),
	                rs.getString("title"),
	                rs.getString("author"),
	                rs.getString("publisher"),
	                rs.getInt("totalCount"),
	                rs.getDate("createAt")
	            );

	            bookList.add(book);
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException("카테고리별 도서 조회 중 오류 발생", e);

	    } finally {
	       ds.closeConnection(con);
	    }

	    return bookList;
	}
	
	
	
	// 도서 키워드로 조회
		// 도서 검색
	public List<BookWithCategoryVO> getSearchBooks(String keyword) {
	    List<BookWithCategoryVO> bookList = new ArrayList<>();
	    Connection con = null;

	    String sql = "SELECT "
	               + "  book_id AS bookId, "
	               + "  category_id AS categoryId, "
	               + "  category_name AS categoryName, "
	               + "  title AS title, "
	               + "  author AS author, "
	               + "  publisher AS publisher, "
	               + "  total_count AS totalCount, "
	               + "  create_at AS createAt "
	               + "FROM book_with_category_view "
	               + "WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ?";

	    try {
	        con = ds.getConnection();
	        PreparedStatement stmt = con.prepareStatement(sql);
	        
	        String searchPattern = "%" + keyword.toLowerCase() + "%";
	        
	        stmt.setString(1, searchPattern);
	        stmt.setString(2, searchPattern);

	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            BookWithCategoryVO book = new BookWithCategoryVO(
	                rs.getInt("bookId"),
	                rs.getInt("categoryId"),
	                rs.getString("categoryName"),
	                rs.getString("title"),
	                rs.getString("author"),
	                rs.getString("publisher"),
	                rs.getInt("totalCount"),
	                rs.getDate("createAt")
	            );
	            bookList.add(book);
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException("🔍 도서 검색 중 오류 발생: " + e.getMessage(), e);
	    } finally {
		      ds.closeConnection(con);
		 }
	    
	    return bookList;
	}
	
	

	// 도서 등록
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
	
	
	/* 도서 목록 조회 */
	public List<BookWithCategoryVO> getBookAll(){
		
		Connection con = null;
		List<BookWithCategoryVO> bookList = new ArrayList<>();
		
		try {
			
			con = ds.getConnection();
			
			String selectBookAll = "SELECT"
					+ "  book_id AS bookId, "
					+ "  category_id AS categoryId, "
					+ "  name AS categoryName,"
					+ "  title AS title, "
					+ "  author AS author, "
					+ "  publisher AS publisher, "
					+ "  total_count AS totalCount, "
					+ "  create_at AS createAt "
					+ "FROM "
					+ "book_with_category_view";
				
			PreparedStatement stmt = con.prepareStatement(selectBookAll);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				bookList.add(new BookWithCategoryVO(rs.getInt("bookId")
							, rs.getInt("categoryId")
							, rs.getString("categoryName")
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
	
	
	// 선택한 도서 조회
	public BookVO getBookById(int bookId) {
		
		Connection con = null;
		
	    String sql = "SELECT "
	               + "  book_id AS bookId, "
	               + "  category_id AS categoryId, "
	               + "  title AS title, "
	               + "  author AS author, "
	               + "  publisher AS publisher, "
	               + "  total_count AS totalCount, "
	               + "  create_at AS createAt "
	               + "FROM book_with_category_view "
	               + "WHERE book_id = ?";
	    
	    try {
	    	
		    con = ds.getConnection();
	        PreparedStatement stmt = con.prepareStatement(sql);
	    	stmt.setInt(1, bookId);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            BookVO book = new BookVO();
	            book.setBookId(rs.getInt("bookId"));
	            book.setCategoryId(rs.getInt("categoryId"));
	            book.setTitle(rs.getString("title"));
	            book.setAuthor(rs.getString("author"));
	            book.setPublisher(rs.getString("publisher"));
	            book.setTotalCount(rs.getInt("totalCount"));
	            book.setCreateAt(rs.getDate("createAt"));
	            return book;
	        } else {
	            System.out.println("❌ 해당 ID의 도서가 존재하지 않습니다.");
	            return null;
	        }
	    } catch (SQLException e) {
	        throw new RuntimeException("도서 조회 중 오류 발생", e);
	    }finally {
			ds.closeConnection(con);
		}
	}

	
	
	
	/* 도서 수정 */
	public void updateBook(BookVO book) {
		
		Connection con = null;
	    String sql = "UPDATE book SET title = ?, author = ?, publisher = ?, create_at = ?, total_count = ?, category_id = ? WHERE book_id = ?";
	    
	    try{
	    	con = ds.getConnection();
	        PreparedStatement stmt = con.prepareStatement(sql);
	        		
	        stmt.setString(1, book.getTitle());
	        stmt.setString(2, book.getAuthor());
	        stmt.setString(3, book.getPublisher());
	        stmt.setDate(4, book.getCreateAt());
	        stmt.setInt(5, book.getTotalCount());
	        stmt.setInt(6, book.getCategoryId());
	        stmt.setInt(7, book.getBookId());
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }finally {
			ds.closeConnection(con);
		}
	}
	
	
	/* 도서 삭제 */
	public void deleteBookById(int bookId) {
	    Connection con = null;

	    String sql = "DELETE FROM book WHERE book_id = ?";
	    
	    BookVO bookInfo = getBookById(bookId);
	    if(bookInfo == null) {
	    	return;
	    }else {
	        System.out.println("🔄 삭제하는 도서 정보:");
	        System.out.printf("  📕 제목: %s | 👤 작가: %s | 🏢 출판사: %s\n", bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher());
	        System.out.printf("  📅 출판일: %s | 📦 수량: %d | 📂 카테고리 ID: %d\n", 
	        		bookInfo.getCreateAt(), bookInfo.getTotalCount(), bookInfo.getCategoryId());
	    
	        System.out.print("정말 삭제하시겠습니까? (y/n): ");
	        String confirm = scanner.nextLine().trim();

	        if (!confirm.equalsIgnoreCase("Y")) {
	            System.out.println("❎ 삭제가 취소되었습니다.");
	            return;
	        }
	    
	    }

	    try {
	        con = ds.getConnection();
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setInt(1, bookId);

	        int deletedRow = stmt.executeUpdate();

	        if (deletedRow > 0) {
	            System.out.println("✅ 도서가 성공적으로 삭제되었습니다.(ID: " + bookId + ")");
	        } else {
	            System.out.println("❌ 해당 ID의 도서를 찾을 수 없습니다.");
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException("도서 삭제 중 오류 발생", e);

	    } finally {
	    	ds.closeConnection(con);
	    }
	}

	
	

			

}
