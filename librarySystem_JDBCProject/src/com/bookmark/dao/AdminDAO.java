package com.bookmark.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;
import com.bookmark.vo.MajorVO;
import com.bookmark.vo.MemberVO;

public class AdminDAO {

	// DataSource
	DataSource ds = new DataSource();
	// memberVO
	MemberVO memberVO = new MemberVO();
	// majorVO
	MajorVO majorVO = new MajorVO();

	// insert
	public void insertMember() {
		Connection con = null;
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
				majorList();
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
		Connection con = null;
	    System.out.println("===== UPDATE MEMBER (select id and name) =====");
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    int result = 0;
	    String updateMemberSql = "";

	    try {
	        // member list 출력
	        memberList();

	        // db 연결
	        con = ds.getConnection();
	        con.setAutoCommit(false);

	        // 수정 대상 선택
	        System.out.print("Enter the user ID: ");
	        int updateId = Integer.parseInt(ds.sc.nextLine());
	        System.out.print("Enter the user NAME: ");
	        String updateName = ds.sc.nextLine();

	        // DB에서 해당 유저의 현재 정보 조회
	        String selectSql = "SELECT role, major_id FROM member WHERE user_id = ? AND name = ?";
	        pstmt = con.prepareStatement(selectSql);
	        pstmt.setInt(1, updateId);
	        pstmt.setString(2, updateName);
	        rs = pstmt.executeQuery();

	        if (!rs.next()) {
	            System.out.println("No member found with that ID and name.");
	            return;
	        }

	        String currentRole = rs.getString("role");
	        int currentMajorId = rs.getInt("major_id");

	        System.out.println("Current role: " + currentRole + ", current major ID: " + currentMajorId);

	        // 업데이트 확인
	        System.out.print("Are you sure you want to update this member? (y / n): ");
	        String confirm = ds.sc.nextLine();
	        if (confirm.equalsIgnoreCase("n")) {
	            System.out.println("Update canceled.");
	            return;
	        }

	        // 새 정보 입력
	        System.out.print("Enter the new role (student / lib): ");
	        String newRole = ds.sc.nextLine();

	        if (!newRole.equals("student") && !newRole.equals("lib")) {
	            System.out.println("Invalid role.");
	            return;
	        }
	        
	        if (newRole.equals("student")) {
	        	majorList();
	        	
	        	System.out.print("Enter the new major ID: ");
	        	int newMajorId = Integer.parseInt(ds.sc.nextLine());
	        	
	        	// 조건문 비교
	        	if (newMajorId == currentMajorId && newRole.equals(currentRole)) {
	        		System.out.println("❗ No changes detected. Member not updated.");
	        		return;
	        	}
	        	
	        	if (newMajorId % 10 != 0) {
	        		System.out.println("Invalid major ID.");
	        		return;
	        	} else {
	        		// 업데이트 실행
	        		updateMemberSql = "UPDATE member SET role = ?, major_id = ? WHERE user_id = ? AND name = ?";
	        		pstmt = con.prepareStatement(updateMemberSql);
	        		pstmt.setString(1, newRole);
	        		pstmt.setInt(2, newMajorId);
	        		pstmt.setInt(3, updateId);
	        		pstmt.setString(4, updateName);
	        		
	        		result = pstmt.executeUpdate();
	        	} 
	        } else {
        		// 업데이트 실행
        		updateMemberSql = "UPDATE member SET role = ? WHERE user_id = ? AND name = ?";
        		pstmt = con.prepareStatement(updateMemberSql);
        		pstmt.setString(1, newRole);
        		pstmt.setInt(2, updateId);
        		pstmt.setString(3, updateName);
        		
        		result = pstmt.executeUpdate();
	        }

	        if (result > 0) {
	            con.commit();
	            System.out.println("Member updated successfully.");
	            
	        } else {
	            System.out.println("Member update failed.");
	        }

	    } catch (Exception e) {
	        try {
	            if (con != null) con.rollback();
	            System.out.println("❗ Error occurred: " + e.getMessage());
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
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


	// delete member -> select id and name
	public void deleteMember() {
		Connection con = null;
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
	public void memberListFilteringAdmin() {
		Connection con = null;
		System.out.println("===== SHOW ADMIN MEMBER LIST =====");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();

			// sql
			String memberListSql = "SELECT user_id, name, role FROM member where role = 'admin' order by user_id";
			pstmt = con.prepareStatement(memberListSql);
			rs = pstmt.executeQuery();

			System.out.println("[ ADMIN MEMBER LIST ]");
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
	
	// (filtering: admin, lib, student)
	public void memberListFilteringLib() {
		Connection con = null;
		System.out.println("===== SHOW LIBRARIAN MEMBER LIST =====");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();

			// sql
			String memberListSql = "SELECT user_id, name, role FROM member where role = 'lib' order by user_id";
			pstmt = con.prepareStatement(memberListSql);
			rs = pstmt.executeQuery();

			System.out.println("[ LIBRARIAN MEMBER LIST ]");
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
	
	// (filtering: admin, lib, student)
	public void memberListFilteringStudent() {
		Connection con = null;
		System.out.println("===== SHOW STUDENT MEMBER LIST =====");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();

			// sql
			String memberListSql =     "SELECT " +
				    "    member.user_id, " +
				    "    member.name, " +
				    "    member.role, " +
				    "    major.major_name AS major_name " +
				    "FROM " +
				    "    member " +
				    "INNER JOIN " +
				    "    major ON major.major_id = member.major_id " +
				    "WHERE " +
				    "    member.role = 'student'";
			pstmt = con.prepareStatement(memberListSql);
			rs = pstmt.executeQuery();

			System.out.println("[ STUDENT MEMBER LIST ]");
			while (rs.next()) {
				int id = rs.getInt("user_id");
				String name = rs.getString("name");
				String role = rs.getString("role");
				String majorName = rs.getString("major_name");
				System.out.printf("\"ID: %-10d | NAME: %-10s | ROLE: %-7s | MAJOR NAME: %-15s\n", id, name, role, majorName);
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

	// select member -> member list
	public void memberList() {
		Connection con = null;
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
		Connection con = null;
		System.out.println("===== CREATE NEW MAJOR =====");

		PreparedStatement pstmt = null;
		try {
			//SHOW MAJOR LIST
			// 1. department list 출력
			departmentList();
			
			con = ds.getConnection();
			con.setAutoCommit(false);

			System.out.print("SELECT 학부 No: ");
			int parentId = Integer.parseInt(ds.sc.nextLine());
			
			majorListWithDept (parentId);
			
			System.out.println("Enter the new major name: ");
			String majorName = ds.sc.nextLine();
			
			String sqlInsert = "INSERT INTO major (major_id, major_name, parent_id) VALUES (majorNo_seq.NEXTVAL, ?, "+parentId+")";
			pstmt = con.prepareStatement(sqlInsert);
			
			pstmt.setString(1, majorName);

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
		Connection con = null;
	    System.out.println("===== UPDATE MAJOR =====");
	    PreparedStatement pstmt = null;

	    try {
	    	//SHOW MAJOR LIST
			// 1. department list 출력
			departmentList();
			
			con = ds.getConnection();
			con.setAutoCommit(false);

			System.out.print("SELECT 학부 No: ");
			int parentId = Integer.parseInt(ds.sc.nextLine());
			majorListWithDept(parentId);
			
			System.out.print("SELECT 학과 No: ");
			int majorId = Integer.parseInt(ds.sc.nextLine());

	        // 4. 확인
	        System.out.println("Are you sure you want to update this major? " + majorId + " (y / n): ");
	        String confirm = ds.sc.nextLine();
	        if (confirm.equalsIgnoreCase("n")) {
	            System.out.println("update canceled!");
	            return;
	        }

	        // 5. 새로운 이름 입력
	        System.out.println("Enter the new major name: ");
	        String updateName = ds.sc.nextLine();

	        // 6. UPDATE 실행
	        String updateSql = "UPDATE major SET major_name = ? WHERE major_id = ?";
	        pstmt = con.prepareStatement(updateSql);
	        pstmt.setString(1, updateName);
	        pstmt.setInt(2, majorId);

	        int result = pstmt.executeUpdate();
	        if (result > 0) {
	            con.commit();
	            System.out.println("Major updated successfully.");
	        } else {
	            System.out.println("No major found with the provided ID.");
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
	
	//major list -> select dept id
	public void majorListWithDept (int parentId) {
		Connection con = null;
		System.out.println("===== SHOW MAJOR LIST =====");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			
			//sql
			String majorDeptListSql = "SELECT major_id, major_name, parent_id FROM major WHERE parent_id = "+parentId;
			
			pstmt = con.prepareStatement(majorDeptListSql);
			rs = pstmt.executeQuery();
			
			System.out.println("[ MAJOR DEPT LIST ]");
			while(rs.next()) {
				int id = rs.getInt("major_id");
				String majorName = rs.getString("major_name");
				int deptId = rs.getInt("parent_id");
				System.out.printf("MAJOR ID: %-4d | NAME: %-6s| DEPT ID: %-6d\n", id, majorName, deptId);
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


	// major list -> all
	public void majorList() {
		Connection con = null;
		System.out.println("===== SHOW MAJOR LIST =====");
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();

			String sql ="SELECT \r\n"
					+ "    LPAD(' ', LEVEL * 2) || major_name AS major_hierarchy,\r\n"
					+ "    major_id,\r\n"
					+ "    parent_id\r\n"
					+ "FROM major\r\n"
					+ "START WITH parent_id IS NULL\r\n"
					+ "CONNECT BY PRIOR major_id = parent_id";

				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();

				System.out.println("[ MAJOR HIERARCHY LIST ]");
				System.out.println("---------------------------------------------------------------------------------");
				System.out.printf("| %-30s | %-10s | %-10s |\n", "HIERARCHY NAME", "MAJOR ID", "PARENT ID");
				System.out.println("---------------------------------------------------------------------------------");

				while (rs.next()) {
				    String hierarchyName = rs.getString("major_hierarchy");
				    int majorId = rs.getInt("major_id");
				    int parentId = rs.getInt("parent_id"); // 이 값이 null이면 0으로 출력됨 → 아래 참고

				    // null 처리: parent_id가 0이면 "NULL"로 출력
				    String parentIdStr = rs.wasNull() ? " " : String.valueOf(parentId);

				    System.out.printf("| %-30s | %-10d | %-10s |\n", hierarchyName, majorId, parentIdStr);
				}
				System.out.println("---------------------------------------------------------------------------------");

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
	
	public void departmentList() {
		Connection con = null;
		System.out.println("===== SHOW DEPARTMENT LIST =====");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			
			//sql
			String deptListSql = "SELECT major_name, major_id\r\n"
					+ "FROM major\r\n"
					+ "WHERE parent_id IS NULL";
			
			pstmt = con.prepareStatement(deptListSql);
			rs = pstmt.executeQuery();
			
			System.out.println("[ DEPARTMENT LIST ]");
			while(rs.next()) {
				int id = rs.getInt("major_id");
				String majorName = rs.getString("major_name");
				System.out.printf("\"ID: %-10d | NAME: %-10s\n", id, majorName);
			}
		} catch(SQLException e) {
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
		Connection con = null;
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
			System.out.println("Enter the major name to delete: ");
			String deleteName = ds.sc.nextLine();

			// 삭제 확인
			System.out.println("Are you sure you want to delete this major? " + deleteName + " (y / n): ");
			String confirm = ds.sc.nextLine();

			if (confirm.equals("n")) {
				System.out.println("delete canceled!");
				return;
			}

			// 삭제 실행
			String deleteSql = "delete from major where name = ?";
			pstmt = con.prepareStatement(deleteSql);
			pstmt.setString(1, deleteName);

			int result = pstmt.executeUpdate();

			if (result > 0) {
				con.commit();
				System.out.println("Major deleted successfully.");
			} else {
				System.out.println("No Major found with...");
			}
		}catch (Exception e) {
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
