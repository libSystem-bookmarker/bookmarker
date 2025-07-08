package com.bookmark.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;
import com.bookmark.vo.MemberVO;

public class MemberCommonDAO {

	// Connection
	Connection con = null;
	// DataSource
	DataSource ds = new DataSource();
	// memberVO
	MemberVO memberVO = new MemberVO();

	/**
	 * @author ys.kim
	 * @param inputId
	 * @param inputPw
	 * 아이디, 비밀번호를 확인해 로그인 하는 메서드
	 */
	public boolean loginMember(String inputId, String inputPw) {

		try {
			con = ds.getConnection();
			// login sql
			String sqlBooleanLogin = "SELECT * FROM MEMBER WHERE user_id = ? and pw = ?";
			// preparedStatement
			PreparedStatement pstmt = con.prepareStatement(sqlBooleanLogin);
			pstmt.setInt(1, Integer.parseInt(inputId));
			pstmt.setString(2, inputPw);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				memberVO.setUser_id(rs.getInt("user_id")); // userId
				memberVO.setName(rs.getString("name")); // user name
				memberVO.setPw(rs.getString("pw")); // pw
				memberVO.setRole(rs.getString("role")); // admin lib student
				memberVO.setPhone_number(rs.getString("phone_number"));
				memberVO.setAddress(rs.getString("address"));
				memberVO.setMajor_id(rs.getInt("major_id"));

				Session.loggedInUser = memberVO; // 로그인된 사용자 세션 저장
				System.out.println("login success : [" + memberVO.getName() + " " + memberVO.getRole() + "]");
				return true;
			} else {
				System.out.println("로그인 실패: 아이디 또는 비밀번호 오류");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ds.closeConnection(con); // close Connection

		}
		return false;
	}

	/**
	 * @author ys.kim
	 * 현재 로그인 한 사용자의 상세 정보를 출력하는 메서드
	 */
	public void userDetails() {
		System.out.println("===== USER DETAIL INFORMATION =====");
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			int userId = Session.loggedInUser.getUser_id();

			// sql
			String memberDetailSql = "SELECT " + "    member.user_id, " + "    member.name, " + "    member.role, "
					+ "    major.major_name AS major_name " + "FROM " + "    member " + "INNER JOIN "
					+ "    major ON major.major_id = member.major_id " + "WHERE " + "    member.user_id=" + userId;
			pstmt = con.prepareStatement(memberDetailSql);
			rs = pstmt.executeQuery();

			System.out.println("[ your information ]");
			while (rs.next()) {
				int id = rs.getInt("user_id");
				String name = rs.getString("name");
				String role = rs.getString("role");
				System.out.printf("ID: %-10d | NAME: %-10s | ROLE: %-7s\n", id, name, role);
			}
		} catch (SQLException e) {
			System.out.println("Failed to fetch member information: " + e.getMessage());
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
	
	/**
	 * @author ys.kim
	 * 현재 로그인한 사용자의 롤에 따라 한정된 정보 수정
	 */
	public void updateMember() {
	    Connection con = null;
	    System.out.println("===== UPDATE USER INFO =====");
	    PreparedStatement pstmt = null;

	    try {
	        // DB 연결
	        con = ds.getConnection();
	        con.setAutoCommit(false); // 트랜잭션 시작

	        // 로그인 유저 정보
	        int currentUserId = Session.loggedInUser.getUser_id();

	        // 비밀번호 확인
	        System.out.println("Enter your password to access the member information update page.");
	        String checkPw = ds.sc.nextLine();

	        if (!checkPw.equals(Session.loggedInUser.getPw())) {
	            System.out.println(" Password does not match.");
	            return;
	        }

	        // 수정 여부 확인
	        System.out.println("Are you sure you want to update your information?  (y / n): ");
	        String confirm = ds.sc.nextLine();
	        if (confirm.equalsIgnoreCase("n")) {
	            System.out.println("❗ Update canceled.");
	            return;
	        }

	        // 새 정보 입력
	        System.out.println("Enter the new Name: ");
	        String newName = ds.sc.nextLine();
	        System.out.println("Enter the new Password: ");
	        String newPw = ds.sc.nextLine();
	        System.out.println("Enter the new Phone Number: ");
	        String newTel = ds.sc.nextLine();
	        System.out.println("Enter the new Address: ");
	        String newAddress = ds.sc.nextLine();

	        // SQL 문법 수정 (SET 다음에만 컬럼 나열)
	        String updateUserSql = "UPDATE member SET name = ?, pw = ?, phone_number = ?, address = ? WHERE user_id = ?";
	        pstmt = con.prepareStatement(updateUserSql);
	        pstmt.setString(1, newName);
	        pstmt.setString(2, newPw);
	        pstmt.setString(3, newTel);
	        pstmt.setString(4, newAddress);
	        pstmt.setInt(5, currentUserId);

	        int result = pstmt.executeUpdate();

	        if (result > 0) {
	            con.commit();
	            System.out.println("Member information updated successfully.");
	            // 세션 정보갱신
	            Session.loggedInUser.setName(newName);
	            Session.loggedInUser.setPw(newPw);
	            Session.loggedInUser.setPhone_number(newTel);
	            Session.loggedInUser.setAddress(newAddress);
	        } else {
	            System.out.println("No member found with the provided ID.");
	        }

	    } catch (Exception e) {
	        try {
	            if (con != null) con.rollback();
	            System.out.println("❗ Error occurred: " + e.getMessage());
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    } finally {
	        try {
	            if (pstmt != null) pstmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        ds.closeConnection(con);
	    }
	}
}
