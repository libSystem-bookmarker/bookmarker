package com.bookmark.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bookmark.common.DataSource;
import com.bookmark.vo.MajorVO;
import com.bookmark.vo.MemberVO;

public class AdminDAO {

	// Connection
	Connection con = null;
	// DataSource
	DataSource ds = new DataSource();
	// memberVO
	MemberVO memberVO = new MemberVO();
	// majorVO
	MajorVO majorVO = new MajorVO();

	// insert
	public void insertMember() {
		System.out.println("===== create new member =====");
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);

			System.out.println("name: ");
			memberVO.setName(ds.sc.nextLine());
			System.out.println("role (student / lib): ");
			memberVO.setRole(ds.sc.nextLine());
			if (memberVO.getRole().equals("student") || memberVO.getRole().equals("lib")) {
				System.out.println("phone_number: ");
				memberVO.setPhone_number(ds.sc.nextLine());
				System.out.println("address: ");
				memberVO.setAddress(ds.sc.nextLine());
				System.out.println("major id: (10,20,30,...150)");
				memberVO.setMajor_id(Integer.parseInt(ds.sc.nextLine()));
				if (memberVO.getMajor_id() % 10 != 0) {
					System.out.println("fail");
				} else {

					String sqlInsert = "INSERT INTO MEMBER (user_id, pw, name, role, phone_number, address, major_id) VALUES (memberNo_seq.nextval,'1234', ?,?,?,?,?)";
					pstmt = con.prepareStatement(sqlInsert);
					pstmt.setString(1, memberVO.getName());
					pstmt.setString(2, memberVO.getRole());
					pstmt.setString(3, memberVO.getPhone_number());
					pstmt.setString(4, memberVO.getAddress());
					pstmt.setInt(5, memberVO.getMajor_id());

					int result = pstmt.executeUpdate();
					if (result > 0) {
						con.commit();
						System.out.println("Member inserted successfully.");
					} else {
						System.out.println("Member insert failed.");
					}
				}

			}

		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback();
				System.out.println("Rollback due to error: " + e.getMessage());
			} catch (Exception rollbackEx) {
				rollbackEx.printStackTrace();
			}
			e.printStackTrace();

		} finally {
			try {
				if (pstmt != null)
					pstmt.close(); // 자원 정리
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ds.closeConnection(con); // 커넥션 반환
		}
	}

	// update member -> select id and name
	public void updateMember() {
		System.out.println("===== update member (select id and name) =====");

	}

	// delete member -> select id and name
	public void deleteMember() {
		System.out.println("===== delete member (select id and name) =====");
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 회원 목록 출력
			memberList();

			// connection
			con = ds.getConnection();
			con.setAutoCommit(false);

			// 삭제 대상 선택
			System.out.println("Enter the user ID and user NAME to delete: ");
			int deleteId = Integer.parseInt(ds.sc.nextLine());
			String deleteName = ds.sc.nextLine();

			// 삭제 확인
			System.out.println("Are you sure you want to delete this member? " + deleteName + " (y / n): ");
			String confirm = ds.sc.nextLine();

			if (confirm.equals("n")) {
				System.out.println("delete canceled!");
				return;
			}

			// 삭제 실행
			String deleteSql = "delete from member where user_id = ? and name = ?";
			pstmt = con.prepareStatement(deleteSql);
			pstmt.setInt(1, deleteId);
			pstmt.setString(2, deleteName);

			int result = pstmt.executeUpdate();

			if (result > 0) {
				con.commit();
				System.out.println("Member deleted successfully.");
			} else {
				System.out.println("No member found with...");
			}
		} catch (SQLException e) {
			try {
				if (con != null) {
					con.rollback();
					System.out.println("Rollback due to error: " + e.getMessage());
				}
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ds.closeConnection(con);
		}
	}

	// (filtering: admin, lib, student)
	public void memberListFilteringRole() {

	}

	// select member -> member list
	public void memberList() {
		System.out.println("===== SHOW MEMBER LIST =====");
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();

			// sql
			String memberListSql = "SELECT user_id, name, role FROM member order by user_id";
			pstmt = con.prepareStatement(memberListSql);
			rs = pstmt.executeQuery();

			System.out.println("[ MEMBER LIST ]");
			while (rs.next()) {
				int id = rs.getInt("user_id");
				String name = rs.getString("name");
				String role = rs.getString("role");
				System.out.printf("\"ID: %-10d | NAME: %-10s | ROLE: %-7s\n", id, name, role);
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

	// insert major
	public void insertMajor() {
		System.out.println("===== CREATE NEW MAJOR =====");

		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			con.setAutoCommit(false);

			System.out.print("major name: ");
			majorVO.setName(ds.sc.nextLine());

			String sqlInsert = "INSERT INTO MAJOR (major_id, name) VALUES (majorNo_seq.nextval,?)";
			pstmt = con.prepareStatement(sqlInsert);
			pstmt.setString(1, majorVO.getName());

			int result = pstmt.executeUpdate();

			if (result > 0) {
				con.commit(); // 커밋 추가
				System.out.println("Major inserted successfully.");
			} else {
				System.out.println("Major insert failed.");
			}

		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback(); // 롤백 추가
				System.out.println("Rollback due to error: " + e.getMessage());
			} catch (Exception rollbackEx) {
				rollbackEx.printStackTrace();
			}
			e.printStackTrace();

		} finally {
			try {
				if (pstmt != null)
					pstmt.close(); // 자원 정리
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ds.closeConnection(con); // 커넥션 반환
		}
	}

	// update major -> select id and name
	public void updateMajor() {
		System.out.println("===== UPDATE MAJOR =====");

	}

	// major list -> all
	public void majorList() {
		System.out.println("===== SHOW MAJOR LIST =====");
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();

			// sql
			String majorListSql = "select major_id, name from major";
			pstmt = con.prepareStatement(majorListSql);
			rs = pstmt.executeQuery();

			System.out.println("[ MAJOR LIST ]");
			while (rs.next()) {
				int id = rs.getInt("major_id");
				String majorName = rs.getString("name");
				System.out.printf("\"ID: %-10d | NAME: %-10s", id, majorName);
			}
		} catch (SQLException e) {
			System.out.println("Failed to fetch major list: " + e.getMessage());
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

	// delete major -> delete id and name
	public void deletemajor() {
		System.out.println("===== DELETE MAJOR =====");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 전공 목록 출력
			majorList();

			// connection
			con = ds.getConnection();
			con.setAutoCommit(false);

			// 삭제 대상
			System.out.println("Enter the major ID and user major to delete: ");
			int deleteId = Integer.parseInt(ds.sc.nextLine());
			String deleteName = ds.sc.nextLine();

			// 삭제 확인
			System.out.println("Are you sure you want to delete this member? " + deleteName + " (y / n): ");
			String confirm = ds.sc.nextLine();

			if (confirm.equals("n")) {
				System.out.println("delete canceled!");
				return;
			}

			// 삭제 실행
			String deleteSql = "delete from member where user_id = ? and name = ?";
			pstmt = con.prepareStatement(deleteSql);
			pstmt.setInt(1, deleteId);
			pstmt.setString(2, deleteName);

			int result = pstmt.executeUpdate();

			if (result > 0) {
				con.commit();
				System.out.println("Member deleted successfully.");
			} else {
				System.out.println("No member found with...");
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}

	}
}
