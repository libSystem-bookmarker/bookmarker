package com.bookmark.common;

import com.bookmark.admin.AdminSelectClass;
import com.bookmark.commondao.MemberCommonDAO;
import com.bookmark.librarian.LibrarianSelectClass;
import com.bookmark.student.StudentSelectClass;

public class ViewClass {

	DataSource ds = new DataSource();
	MemberCommonDAO mcDAO = new MemberCommonDAO();
	AdminSelectClass adminSelect = new AdminSelectClass();
	LibrarianSelectClass librarianSelect = new LibrarianSelectClass();
	StudentSelectClass studentSelect = new StudentSelectClass();

	public void showView() {
		while (true) {
			if (Session.loggedInUser == null) {
				System.out.println("===== library system Login Page =====");
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
