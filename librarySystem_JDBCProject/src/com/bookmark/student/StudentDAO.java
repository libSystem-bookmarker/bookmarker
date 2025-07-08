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

}
