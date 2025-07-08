package com.bookmark.selectview;

import java.sql.SQLException;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;
import com.bookmark.dao.MemberCommonDAO;

public class ViewClass {

	DataSource ds = new DataSource();
	MemberCommonDAO mcDAO = new MemberCommonDAO();
	AdminSelectClass adminSelect = new AdminSelectClass();
	LibrarianSelectClass librarianSelect = new LibrarianSelectClass();
	StudentSelectClass studentSelect = new StudentSelectClass();

	/**
	 * @author ys.kim
	 * 현재 로그인 한 사용자의 role에 따라 출력되는 메뉴가 달라지도록 설정한 반복문
	 * @throws SQLException 
	 */
	public void showView() {
		while (true) {
			if (Session.loggedInUser == null) {

				System.out.println("책갈피에 로그인 해주세요.");
				System.out.println("====================================================================");
				System.out.print("id: ");
				String user_id = ds.sc.nextLine();
				System.out.print("pw: ");
				String pw = ds.sc.nextLine();
				mcDAO.loginMember(user_id, pw);
			}

			if (Session.loggedInUser != null) {
				String userName = Session.loggedInUser.getName();
				String userRole = Session.loggedInUser.getRole();

				if ("admin".equals(userRole)) {
					adminSelect.showViewAdmin(userName, userRole);
				} else if ("lib".equals(userRole)) {
					librarianSelect.showLibrarian(userName, userRole);
				} else if ("student".equals(userRole)) {
					studentSelect.showStudent(userName, userRole);
				}
			}
		}
	}

}
