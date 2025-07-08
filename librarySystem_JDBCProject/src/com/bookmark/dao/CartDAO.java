package com.bookmark.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookmark.common.DataSource;
import com.bookmark.dao.BookDAO;

public class CartDAO {
	
	DataSource ds = new DataSource();
	BookDAO bookDAO = new BookDAO();

	/**
	 * @author ys.kim
	 * @param userId
	 * @param bookId 장바구니에 도서 추가
	 */
	public void insertCart(int userId, int bookId) {
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
			String insertSql = "INSERT INTO cart (cart_id, user_id, book_id, quantity, added_date) "
					+ "VALUES (cart_seq.NEXTVAL, ?, ?, 1, SYSDATE)";
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
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ds.closeConnection(con);
		}
	}

	/**
	 * @author ys.kim
	 * @param userId 세션을 통해 사용자 정보를 가져와 장바구니 내역 조회 및 대출, 반납 기능 구현
	 */
	public void selectCartUserId(int userId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();

			String sql = "SELECT c.cart_id, b.book_id, b.title, b.author, b.publisher, c.quantity, c.added_date "
					+ "FROM cart c " + "JOIN book b ON c.book_id = b.book_id " + "WHERE c.user_id = ?";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();

			List<Integer> bookIdList = new ArrayList<>();
			
			System.out.println("\n\n");
			System.out.println("🛒 장바구니 목록");
			System.out.println("=====================================================================");
			int index = 1;
			while (rs.next()) {
				int bookId = rs.getInt("book_id");
				bookIdList.add(bookId);

				System.out.println(index + ". 도서ID: " + bookId + ", 제목: " + rs.getString("title") + ", 저자: "
						+ rs.getString("author") + ", 출판사: " + rs.getString("publisher") + ", 수량: "
						+ rs.getInt("quantity") + ", 담은 날짜: " + rs.getDate("added_date"));
				index++;
			}

			if (bookIdList.isEmpty()) {
				System.out.println("장바구니가 비어 있습니다.");
				return;
			}

			System.out.print(">> 번호를 선택하세요 (0 입력 시 취소): ");
			int choice = Integer.parseInt(ds.sc.nextLine());

			if (choice == 0 || choice > bookIdList.size()) {
				System.out.println("취소 또는 잘못된 선택입니다.");
				return;
			}

			int selectedBookId = bookIdList.get(choice - 1);

			System.out.print("1. 대출 | 2. 삭제 선택: ");
			int action = Integer.parseInt(ds.sc.nextLine());

			if (action == 1) {
				loanSingleBookFromCart(userId, selectedBookId);
				selectCartUserId(userId); //삭제 후 목록 다시 출력
				return;
			} else if (action == 2) {
				deleteBookFromCart(userId, selectedBookId);
				selectCartUserId(userId); //삭제 후 목록 다시 출력 -> 재귀 호출
				return; //무한 루프를 피하기 위해
			} else {
				System.out.println("잘못된 선택입니다.");
			}

		} catch (SQLException e) {
			System.out.println("오류 발생: " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
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
				if (pstmt != null)
					pstmt.close();
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
				bookDAO.insertLoanBook(bookId, userId);
//
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
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ds.closeConnection(con);
		}
	}

}
