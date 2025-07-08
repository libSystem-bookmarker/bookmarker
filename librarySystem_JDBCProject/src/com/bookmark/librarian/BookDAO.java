package com.bookmark.librarian;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;
import com.bookmark.vo.BookVO;
import com.bookmark.vo.BookWithCategoryVO;
import com.bookmark.vo.BookWithDetailVO;
import com.bookmark.vo.CategoryVO;


public class BookDAO {
	

	DataSource ds = new DataSource();
	Scanner sc = new Scanner(System.in);
	
	
	// 마지막 최근 book id
	/**
	 * 마지막 book id 구하는 메서드
	 * insert 할 때 사용하고 있어서 일단 변경하기는 어려울 듯..
	 * @return 마지막 book_id
	 */
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
	
	
	
	/**
	 * 반납할 도서 목록 조회
	 */
	public List<BookWithDetailVO> getReturnBooks(){
	    Connection con = null;
	    
	    List<BookWithDetailVO> returnList = new ArrayList<>();
	    
	    try {
	        con = ds.getConnection();
	        con.setAutoCommit(false); // 트랜잭션 시작

	        // 1. 도서 ID 또는 제목으로 대출 중인 내역 찾기
	        String sql = "SELECT"
	        		+ "	book_id     AS bookId,"
	        		+ "	title       AS title,"
	        		+ "	author      AS author,"
	        		+ "	publisher   AS publisher,"
	        		+ "	user_id     AS userId,"
	        		+ "	loan_date   AS loanDate,"
	        		+ "	return_date AS returnDate,"
	        		+ "	due_date    AS dueDate,"
	        		+ "	is_overdue  AS isOverdue"
	        		+ "	FROM"
	        		+ "	book_loan_not_returned_view"
	        		+ " WHERE user_id = ?";

	        PreparedStatement stmt = con.prepareStatement(sql);

	        stmt.setInt(1, Session.loggedInUser.getUser_id());           

	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            BookWithDetailVO vo = new BookWithDetailVO();

	            vo.setBookId(rs.getInt("bookId"));
	            vo.setTitle(rs.getString("title"));
	            vo.setAuthor(rs.getString("author"));
	            vo.setPublisher(rs.getString("publisher"));
	            vo.setUserId(rs.getInt("userId"));
	            vo.setLoanDate(rs.getDate("loanDate"));
	            vo.setReturnDate(rs.getDate("returnDate"));
	            vo.setDueDate(rs.getDate("dueDate"));
	            vo.setIsOverdue(rs.getString("isOverdue"));

	            returnList.add(vo);
	        }

	        if (returnList.isEmpty()) {
	            System.out.println("❌ 반납 가능한 대출 내역이 없습니다.");
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException("반납 예정 도서 조회 중 오류 발생", e);

	    } finally {
	        ds.closeConnection(con);
	    }

	    return returnList;

	}
	
	
	
	/**
	 * 도서 반납하기
	 * 반납 가능한 도서 목록 조회
	 * book_loan_detail 반납 내역 업데이트
	 * 책 total_count + 1 증가 
	 */
	public void returnBookById() {
		
	    Connection con = null;

	    // 1. 반납 가능한 도서 목록 조회
	    List<BookWithDetailVO> returnBooks = getReturnBooks();
	    if (returnBooks.isEmpty()) return;

	    // 2. 도서 목록 출력
	    System.out.println("\n📘 반납 가능한 도서 목록:");
	    for (BookWithDetailVO book : returnBooks) {
	        System.out.printf("📗 [도서 ID: %d] 제목: %s | 작가: %s | 출판사: %s | 대출일: %s | 반납 기한: %s \n",
	            book.getBookId(),
	            book.getTitle(),
	            book.getAuthor(),
	            book.getPublisher(),
	            book.getLoanDate().toString(),
	            book.getReturnDate().toString()
	        );
	    }

	    System.out.print("\n반납할 도서 ID를 입력하세요: ");
	    int bookId = sc.nextInt();

	    try {
	        con = ds.getConnection();
	        con.setAutoCommit(false);

	        // 3. 해당 대출 내역 찾기
	        String selectLoanSql = "SELECT book_loan_detail_id, loan_date, return_date FROM book_loan_detail WHERE book_id = ? AND user_id = ? AND due_date IS NULL";
	        PreparedStatement selectStmt = con.prepareStatement(selectLoanSql);
	        selectStmt.setInt(1, bookId);
	        selectStmt.setInt(2, Session.loggedInUser.getUser_id());

	        ResultSet rs = selectStmt.executeQuery();
	        if (!rs.next()) {
	            System.out.println("❌ 해당 도서에 대한 대출 내역이 없습니다.");
	            return;
	        }

	        int loanDetailId = rs.getInt("book_loan_detail_id");
	        Date loanDate = rs.getDate("loan_date");
	        Date returnDate = rs.getDate("return_date");
	        Date today = new Date(System.currentTimeMillis());

	        Calendar cal = Calendar.getInstance();

		     // 연체 기준: loan_date + 7 < today
		     cal.setTime(loanDate);
		     cal.add(Calendar.DATE, 7);
		     Date overdueLimit = new Date(cal.getTimeInMillis());
	
		     // 패널티 기준: return_date + 3 < today
		     cal.setTime(returnDate);
		     cal.add(Calendar.DATE, 3);
		     Date penaltyLimit = new Date(cal.getTimeInMillis());
	
		     String isOverdue = today.after(overdueLimit) ? "Y" : "N";
		     String isPenalized = today.after(penaltyLimit) ? "Y" : "N";

	        // 반납 처리
	        String updateLoanSql = "UPDATE book_loan_detail SET due_date = SYSDATE, is_overdue = ?, is_penalized = ? WHERE book_loan_detail_id = ?";
	        PreparedStatement updateStmt = con.prepareStatement(updateLoanSql);
	        updateStmt.setString(1, isOverdue);
	        updateStmt.setString(2, isPenalized);
	        updateStmt.setInt(3, loanDetailId);
	        updateStmt.executeUpdate();

	        // 도서 수량 복구
	        String updateBookSql = "UPDATE book SET total_count = total_count + 1 WHERE book_id = ?";
	        PreparedStatement bookStmt = con.prepareStatement(updateBookSql);
	        bookStmt.setInt(1, bookId);
	        bookStmt.executeUpdate();

	        con.commit();
	        System.out.printf("✅ 반납 완료! 연체 여부: %s\n", isOverdue);

	    } catch (SQLException e) {
	        try {
	            if (con != null) con.rollback();
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        throw new RuntimeException("도서 반납 중 오류 발생", e);

	    } finally {
	        ds.closeConnection(con);
	    }
		
		
	}
	
	
	
	/**
	 * 도서 대출 내역 추가
	 * 재고 확인 + insert 대출 내역 + update 재고 감소
	 * @param bookId 추가할 도서 id
	 * @param userId 사용자 id
	 */
	public void insertLoanBook(int bookId, int userId) {
	    Connection con = null;
	    
	    System.out.println("insertLoanBook에서 받은 bookId : " + bookId);

	    try {
	        con = ds.getConnection();
	        con.setAutoCommit(false); // 트랜잭션 시작

	        // 1. 재고 확인
	        String checkSql = "SELECT total_count FROM book WHERE book_id = ?";
	        PreparedStatement checkStmt = con.prepareStatement(checkSql);
	        checkStmt.setInt(1, bookId);
	        ResultSet rs = checkStmt.executeQuery();

	        if (!rs.next()) {
	            System.out.println("❌ 존재하지 않는 도서입니다.");
	            return;
	        }

	        int totalCount = rs.getInt("total_count");
	        if (totalCount <= 0) {
	            System.out.println("❌ 대출할 수 있는 수량이 없습니다.(0 권)");
	            return;
	        }

	        // 2. 대출 내역 추가
	        String loanSql = "INSERT INTO book_loan_detail (book_loan_detail_id, user_id, book_id, loan_date, return_date) VALUES (book_loan_detailNo_seq.nextval, ?, ?, SYSDATE, SYSDATE + 7)";
	        PreparedStatement loanStmt = con.prepareStatement(loanSql);
	        loanStmt.setInt(1, userId);
	        loanStmt.setInt(2, bookId);
	        loanStmt.executeUpdate();
//
//	        // 3. 도서 수량 감소
	        String updateSql = "UPDATE book SET total_count = total_count - 1 WHERE book_id = ?";
	        PreparedStatement updateStmt = con.prepareStatement(updateSql);
	        updateStmt.setInt(1, bookId);
	        updateStmt.executeUpdate();

	        con.commit(); // 성공 시 커밋
	        System.out.println("✅ 도서가 성공적으로 대출되었습니다!");
	        
	     
//	        BookVO loanBook = getBookById(bookId);
//	        
//	        System.out.printf("  📘 [ID: %d]  📕 제목: %s | 👤 작가: %s | 🏢 출판사: %s\n", loanBook.getTitle(), loanBook.getAuthor(), loanBook.getPublisher());
//	        System.out.printf("  📅 출판일: %s | 📦 수량: %d | 📂 카테고리 ID: %d\n", 
//	        		loanBook.getCreateAt(), loanBook.getTotalCount(), loanBook.getCategoryId());
	       

	    } catch (SQLException e) {
	        try {
	            if (con != null) con.rollback(); // 실패 시 롤백
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        throw new RuntimeException(e); // 예외

	    } finally {
	        ds.closeConnection(con);
	    }
	}

	
	
	
	
	/**
	 * 카테고리별 도서 목록 조회
	 * @param categoryId
	 * @return 카테고리별 도서 목록 List<BookWithCategoryVO>
	 */
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
	
	
	
	/**
	 * 제목이나 작가명으로 도서 목록 조회 
	 * @param keyword
	 * @return keyword를 포함한 도서 목록 List<BookWithCategoryVO>
	 */
	// 도서 키워드로 조회
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
	
	

	/**
	 * 도서 등록
	 * @param book 입력한 도서 정보
	 */
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
//			throw new RuntimeException(e);

			System.out.println("❌ 입력 오류: " + e.getMessage());
			System.out.println("도서 등록에 실패했습니다.");
		}finally {
			ds.closeConnection(con);
		}
		
	}
	
	
	
	/**
	 * 카테고리 목록 조회
	 * @return 카테고리 전체 목록 List<CategoryVO>
	 */
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
	
	

	/**
	 * 카테고리 조인한 도서 목록 전체 조회
	 * @return  도서 목록 전체 List<BookWithCategoryVO>
	 */
	public List<BookWithCategoryVO> getBookAll(){
		
		Connection con = null;
		List<BookWithCategoryVO> bookList = new ArrayList<>();
		
		try {
			
			con = ds.getConnection();
			
			String selectBookAll = "SELECT"
					+ "  book_id AS bookId, "
					+ "  category_id AS categoryId, "
					+ "  category_name AS categoryName,"
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
	
	
	
	/**
	 * bookId에 따른 도서 한 권 조회
	 * @param bookId
	 * @return 도서 한 권 BookVO
	 */
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

	
	
	
	/**
	 * 도서 수정
	 * @param book 
	 */
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
	
	

	/**
	 * 도서 삭제
	 * bookId에 해당하는 도서 삭제
	 * @param bookId
	 */
	public void deleteBookById(int bookId) {
	    Connection con = null;

	    
	    
	    BookVO bookInfo = getBookById(bookId);
	    if(bookInfo == null) {
	    	return;
	    }else {
	        System.out.println("🔄 삭제하는 도서 정보:");
	        System.out.printf("  📕 제목: %s | 👤 작가: %s | 🏢 출판사: %s\n", bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher());
	        System.out.printf("  📅 출판일: %s | 📦 수량: %d | 📂 카테고리 ID: %d\n", 
	        		bookInfo.getCreateAt(), bookInfo.getTotalCount(), bookInfo.getCategoryId());
	    
	        System.out.print("정말 삭제하시겠습니까? (y/n): ");
	        String confirm = sc.nextLine().trim();

	        if (!confirm.equalsIgnoreCase("Y")) {
	            System.out.println("❎ 삭제가 취소되었습니다.");
	            return;
	        }
	    
	    }
	    

	    try {
	        con = ds.getConnection();
	        
	        String sql = "DELETE FROM book WHERE book_id = ?";
	        
	        PreparedStatement stmt = con.prepareStatement(sql);
	        
	        stmt.setInt(1, bookId);

	        int deletedRow = stmt.executeUpdate();
	        
	        System.out.println("deletedRow : " + deletedRow);
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
