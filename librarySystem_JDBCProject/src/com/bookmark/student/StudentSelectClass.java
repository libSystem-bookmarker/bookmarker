package com.bookmark.student;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;
import com.bookmark.commondao.MemberCommonDAO;

public class StudentSelectClass {
	
	DataSource ds = new DataSource();
	StudentDAO studentDAO = new StudentDAO();
	MemberCommonDAO mcDAO = new MemberCommonDAO();
	
	//student's select
		public void managingStudent() {
			System.out.println(
					Session.loggedInUser.getName()+"'s page | 1. MY INFORMATION | 2. UPDATE MY INFORMATION | 3. RETURN TO STUDENT PAGE");
			int studentMenu = Integer.parseInt(ds.sc.nextLine());
			switch (studentMenu) {
			case 1: {
				mcDAO.userDetails();
				break;
			}
			case 2: {
				mcDAO.updateMember();
				break;
			}
			case 3: {
				System.out.println("return to my information page");
				return; }

			default:
				System.out.println("잘못된 입력입니다.");

			}
		}
		
		public void managingBookInfo() {
			System.out.println(
					Session.loggedInUser.getName()+"'s page | 1. MY LOAN LIST | 2. MY RETURN LIST MY OVERDUE LIST | 3. MY OVERDUE LIST | 4. RETURN TO STUDENT PAGE");
			int studentMenu = Integer.parseInt(ds.sc.nextLine());
			switch (studentMenu) {
			case 1: {
				break;
			}
			case 2: {
				break;
			}
			case 3: {
				break;
			}
			case 4: {
				System.out.println("return to my book info page");
				return; }

			default:
				System.out.println("잘못된 입력입니다.");

			}
		}

		public void showStudent(String userName, String userRole) {
			// if member role == admin
			// else if member role == student
			System.out.println(
					"STUDENT MENU | 1. MY INFORMATION | 2. MY LIBRARY INFORMATION | 3. LOG OUT ");
			int studentMenu = Integer.parseInt(ds.sc.nextLine());
			switch (studentMenu) {
			case 1: {
				managingStudent();
				break;
			}
			case 2: {
				managingBookInfo();
				break;
			}
			case 3:  {
				System.out.println(userName + " user log out: " + userRole);
				Session.loggedInUser = null;
				break;
			}
			default:
				System.out.println("잘못된 입력입니다.");

			}
		}

}
