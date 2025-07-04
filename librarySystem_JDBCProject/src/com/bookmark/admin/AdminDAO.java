package com.bookmark.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;
import com.bookmark.commondao.MemberCommonDAO;
import com.bookmark.vo.MemberVO;

public class AdminDAO {

	// Connection
	Connection con = null;
	// DataSource
	DataSource ds = new DataSource();
	// memberVO
	MemberVO memberVO = new MemberVO();
	// commonDAO
	MemberCommonDAO mcd = new MemberCommonDAO();
	Scanner sc = new Scanner(System.in);

	// insert
	/**
	 * @author yeonsoo.kim
	 * user의 role에 따라 회원 가입 허용
	 */
	public void insertMember() {
		// login한 사용자의 role 정보가 admin일 때
		if (Session.loggedInUser != null && "admin".equals(Session.loggedInUser.getRole())) {
			System.out.println("===== new member =====");
			try {
				con = ds.getConnection();

				System.out.println("name: ");
				memberVO.setName(sc.nextLine());
				System.out.println("role (student / lib): ");
				memberVO.setRole(sc.nextLine());
				if (memberVO.getRole().equals("student") || memberVO.getRole().equals("lib")) {
					System.out.println("phone_number: ");
					memberVO.setPhone_number(sc.nextLine());
					System.out.println("address: ");
					memberVO.setAddress(sc.nextLine());
					System.out.println("major id: (10,20,30,...150)");
					memberVO.setMajor_id(Integer.parseInt(sc.nextLine()));
					if (memberVO.getMajor_id() % 10 != 0) {
						System.out.println("fail");
					} else {

						String sqlInsert = "INSERT INTO MEMBER (user_id, pw, name, role, phone_number, address, major_id) VALUES (memberNo_seq.nextval,'1234', ?,?,?,?,?)";
						PreparedStatement pstmt = con.prepareStatement(sqlInsert);
//				stmt.setDate(0, null); // 입사일은 현재 날짜로
						pstmt.setString(1, memberVO.getName());
						pstmt.setString(2, memberVO.getRole());
						pstmt.setString(3, memberVO.getPhone_number());
						pstmt.setString(4, memberVO.getAddress());
						pstmt.setInt(5, memberVO.getMajor_id());

						int result = pstmt.executeUpdate();
						if (result > 0) {
							System.out.println("success");
						} else {
							System.out.println("fail");
						}
					}

				} else {
					System.out.println("wrong role");

				}

			} catch (SQLException e) {
				e.printStackTrace();

			} finally {
				ds.closeConnection(con);
			}
		}
	}

}
