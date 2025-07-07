package com.bookmark.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.bookmark.common.DataSource;
import com.bookmark.vo.MajorVO;
import com.bookmark.vo.MemberVO;

public class StudentDAO {

	// DataSource
	DataSource ds = new DataSource();
	// memberVO
	MemberVO memberVO = new MemberVO();
	// majorVO
	MajorVO majorVO = new MajorVO();

	/**
	 * @author ys.kim
	 * @param userId 사용자 아이디를 통해 현재 로그인한 사용자가 대여한 책 목록을 조회하는 메서드
	 */
	public void loanedBookList(int userId) {
		Connection con = null;
		System.out.println("===== SHOW loanedBookList =====");
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();

			// sql
			// String select loaned book -> if return data is null
			String loandedListSql = "SELECT\n" + "    bld.book_loan_detail_id       AS 대출번호,\n"
					+ "    b.title                       AS 제목,\n" + "    b.author                      AS 작가,\n"
					+ "    b.publisher                   AS 출판사,\n" + "    bld.loan_date                 AS 대출일,\n"
					+ "    bld.due_date                  AS 실제반납일,\n" + "    bld.return_date               AS 반납예정일,\n"
					+ "    CASE\n" + "        WHEN bld.due_date IS NULL THEN 'X'\n" + "        ELSE 'O'\n"
					+ "    END                           AS 반납상태,\n" + "    bld.is_overdue                AS 연체여부,\n"
					+ "    bld.is_penalized              AS \"잔여 연체일 여부\"\n" + "FROM\n" + "    book_loan_detail bld\n"
					+ "JOIN\n" + "    book b ON b.book_id = bld.book_id\n" + "WHERE\n" + "    bld.user_id = ?\n"
					+ "ORDER BY\n" + "    bld.loan_date DESC";

			pstmt = con.prepareStatement(loandedListSql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();

			// 헤더
			System.out.printf("%-8s | %-15s | %-8s | %-10s | %-12s | %-13s | %-13s | %-8s | %-8s | %-10s\n", "대출번호",
					"제목", "작가", "출판사", "대출일", "반납예정일", "실제반납일", "반납", "연체", "패널티");
			System.out.println("-".repeat(120));

			// 데이터 출력
			while (rs.next()) {
				int id = rs.getInt("대출번호");
				String title = rs.getString("제목");
				String author = rs.getString("작가");
				String publisher = rs.getString("출판사");
				Date loanDate = rs.getDate("대출일");
				Date returnDate = rs.getDate("반납예정일");
				Date dueDate = rs.getDate("실제반납일");
				String returnStatus = rs.getString("반납상태");
				String overdue = rs.getString("연체여부");
				String penalized = rs.getString("잔여 연체일 여부");

				System.out.printf("%-8d | %-15s | %-8s | %-10s | %-12s | %-13s | %-13s | %-8s | %-8s | %-10s\n", id,
						truncate(title, 15), truncate(author, 8), truncate(publisher, 10), formatDate(loanDate),
						formatDate(returnDate), formatDate(dueDate), returnStatus, overdue, penalized);
			}

		} catch (SQLException e) {
			System.out.println("Failed to fetch member list: " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ds.closeConnection(con);
		}

	}

	// 날짜 포맷 yyyy-MM-dd
	private String formatDate(Date date) {
		if (date == null)
			return "null";
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	// 문자열 자르기
	private String truncate(String str, int maxLength) {
		if (str == null)
			return "-";
		return str.length() > maxLength ? str.substring(0, maxLength - 1) + "…" : str;
	}

	/**
	 * @author ys.kim
	 * @param userId
	 * @param bookId
	 * 장바구니에 도서 추가
	 */
	public void addBookToCart(int userId, int bookId) {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        con = ds.getConnection();

	        // 1. 장바구니 수량 확인
	        String countCartSql = "SELECT COUNT(*) FROM cart WHERE user_id = ?";
	        pstmt = con.prepareStatement(countCartSql);
	        pstmt.setInt(1, userId);
	        rs = pstmt.executeQuery();
	        int cartCount = 0;

	        if (rs.next()) {
	            cartCount = rs.getInt(1);
	        }

	        rs.close();
	        pstmt.close();

	        if (cartCount >= 5) {
	            System.out.println("장바구니에는 최대 5권까지만 담을 수 있습니다.");
	            return;
	        }

	        // 2. 이미 담겨 있는 책인지 확인
	        String duplicateCheckSql = "SELECT COUNT(*) FROM cart WHERE user_id = ? AND book_id = ?";
	        pstmt = con.prepareStatement(duplicateCheckSql);
	        pstmt.setInt(1, userId);
	        pstmt.setInt(2, bookId);
	        rs = pstmt.executeQuery();

	        if (rs.next() && rs.getInt(1) > 0) {
	            System.out.println("이 도서는 이미 장바구니에 담겨 있습니다.");
	            return;
	        }

	        rs.close();
	        pstmt.close();

	        // 3. 장바구니에 추가
	        String insertSql = "INSERT INTO cart (cart_id, user_id, book_id, quantity, added_date) " +
	                           "VALUES (cart_seq.NEXTVAL, ?, ?, 1, SYSDATE)";
	        pstmt = con.prepareStatement(insertSql);
	        pstmt.setInt(1, userId);
	        pstmt.setInt(2, bookId);

	        int inserted = pstmt.executeUpdate();

	        if (inserted > 0) {
	            System.out.println("도서가 장바구니에 추가되었습니다.");
	        }

	    } catch (SQLException e) {
	        System.out.println("장바구니 추가 중 오류 발생: " + e.getMessage());
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        ds.closeConnection(con);
	    }
	}


	/**
	 * @author ys.kim
	 * @param userId
	 * 세션을 통해 사용자 정보를 가져와 장바구니 내역 조회 및 대출, 반납 기능 구현
	 */
	public void viewCartAndSelectOption(int userId) {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        con = ds.getConnection();

	        String sql = "SELECT c.cart_id, b.book_id, b.title, b.author, b.publisher, c.quantity, c.added_date " +
	                     "FROM cart c " +
	                     "JOIN book b ON c.book_id = b.book_id " +
	                     "WHERE c.user_id = ?";

	        pstmt = con.prepareStatement(sql);
	        pstmt.setInt(1, userId);
	        rs = pstmt.executeQuery();

	        List<Integer> bookIdList = new ArrayList<>();
	        System.out.println("===== 장바구니 목록 =====");
	        int index = 1;
	        while (rs.next()) {
	            int bookId = rs.getInt("book_id");
	            bookIdList.add(bookId);

	            System.out.println(index + ". 도서ID: " + bookId +
	                ", 제목: " + rs.getString("title") +
	                ", 저자: " + rs.getString("author") +
	                ", 출판사: " + rs.getString("publisher") +
	                ", 수량: " + rs.getInt("quantity") +
	                ", 담은 날짜: " + rs.getDate("added_date"));
	            index++;
	        }

	        if (bookIdList.isEmpty()) {
	            System.out.println("장바구니가 비어 있습니다.");
	            return;
	        }

	        System.out.print(">> 번호를 선택하세요 (0 입력 시 취소): ");
	        int choice = Integer.parseInt(ds.sc.nextLine());
	        ds.sc.nextLine(); // 버퍼 정리

	        if (choice == 0 || choice > bookIdList.size()) {
	            System.out.println("취소 또는 잘못된 선택입니다.");
	            return;
	        }

	        int selectedBookId = bookIdList.get(choice - 1);

	        System.out.print("1. 대출 / 2. 삭제 선택: ");
	        int action = Integer.parseInt(ds.sc.nextLine());

	        if (action == 1) {
	            loanSingleBookFromCart(userId, selectedBookId);
	        } else if (action == 2) {
	            deleteBookFromCart(userId, selectedBookId);
	        } else {
	            System.out.println("잘못된 선택입니다.");
	        }

	    } catch (SQLException e) {
	        System.out.println("오류 발생: " + e.getMessage());
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        ds.closeConnection(con);
	    }
	}
	
	public void deleteBookFromCart(int userId, int bookId) {
	    Connection con = null;
	    PreparedStatement pstmt = null;

	    try {
	        con = ds.getConnection();

	        String deleteSql = "DELETE FROM cart WHERE user_id = ? AND book_id = ?";
	        pstmt = con.prepareStatement(deleteSql);
	        pstmt.setInt(1, userId);
	        pstmt.setInt(2, bookId);
	        int deleted = pstmt.executeUpdate();

	        if (deleted > 0) {
	            System.out.println("장바구니에서 삭제되었습니다.");
	        } else {
	            System.out.println("삭제할 항목이 없습니다.");
	        }

	    } catch (SQLException e) {
	        System.out.println("삭제 중 오류 발생: " + e.getMessage());
	    } finally {
	        try {
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        ds.closeConnection(con);
	    }
	}
	public void loanSingleBookFromCart(int userId, int bookId) {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        con = ds.getConnection();

	        // 1. 잔여 수량 확인
	        String checkSql = "SELECT total_count FROM book WHERE book_id = ?";
	        pstmt = con.prepareStatement(checkSql);
	        pstmt.setInt(1, bookId);
	        rs = pstmt.executeQuery();

	        if (rs.next() && rs.getInt(1) > 0) {
	            rs.close();
	            pstmt.close();

	            // 2. 대출 처리
	            String loanSql = "INSERT INTO book_loan_detail (book_loan_detail_id, book_id, user_id, loan_date, due_date, is_overdue, is_penalized) " +
	                             "VALUES (book_loan_detail_seq.NEXTVAL, ?, ?, SYSDATE, SYSDATE + 7, 'N', 'N')";
	            pstmt = con.prepareStatement(loanSql);
	            pstmt.setInt(1, bookId);
	            pstmt.setInt(2, userId);
	            pstmt.executeUpdate();
	            pstmt.close();

	            // 3. 책 수량 -1
	            String updateBookSql = "UPDATE book SET total_count = total_count - 1 WHERE book_id = ?";
	            pstmt = con.prepareStatement(updateBookSql);
	            pstmt.setInt(1, bookId);
	            pstmt.executeUpdate();
	            pstmt.close();

	            // 4. 장바구니에서 제거
	            String deleteCartSql = "DELETE FROM cart WHERE user_id = ? AND book_id = ?";
	            pstmt = con.prepareStatement(deleteCartSql);
	            pstmt.setInt(1, userId);
	            pstmt.setInt(2, bookId);
	            pstmt.executeUpdate();

	            System.out.println("Loan successful and removed from cart.");
	        } else {
	            System.out.println("No copies left to loan.");
	        }

	    } catch (SQLException e) {
	        System.out.println("Failed to loan book from cart: " + e.getMessage());
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        ds.closeConnection(con);
	    }
	}


}
