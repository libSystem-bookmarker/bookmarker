package com.bookmark.commondao;

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

	// login 판단 & get user role
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
				System.out.println("로그인 성공! [" + memberVO.getRole() + "]");
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
	

}
