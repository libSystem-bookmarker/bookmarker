package com.bookmark.student;

import java.sql.SQLException;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;
import com.bookmark.commondao.MemberCommonDAO;
import com.bookmark.librarian.BookDAO;
import com.bookmark.loan.LoanSystem;

public class StudentSelectClass {
	
	DataSource ds = new DataSource();
	StudentDAO studentDAO = new StudentDAO();
	BookDAO bookDao = new BookDAO();
	MemberCommonDAO mcDAO = new MemberCommonDAO();

	//CartDAO cartDAO = new CartDAO();

	LoanSystem loanSystem = new LoanSystem();

	
		/**
		 * @author ys.kim
		 * 학생 정보 수정 및 조회하는 switch 문
		 */
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
		
		/**
		 * @author ys.kim
		 * cart 조회, 삭제, 대여
		 */
		public void cartInfo () {
			
			
		}
		
		/**
		 * @author ys.kim
		 * 학생이 대여한 정보 조회 및 장바구니 기능 switch 문
		 * @throws SQLException 
		 */
		public void managingBookInfo() {
			
			//get user information
			int userId = Session.loggedInUser.getUser_id();
			System.out.println(
					Session.loggedInUser.getName()+"'s page | 1. MY LOAN LIST | 2. My Cart | 4. RETURN TO STUDENT PAGE");
			int studentMenu = Integer.parseInt(ds.sc.nextLine());
			switch (studentMenu) {
			case 1: { studentDAO.loanedBookList(userId);
				break;
			}
			case 2: { studentDAO.viewCartAndSelectOption(userId);
				break;
			}
			case 3: { //cartDAO.getCartByUserId(userId);
				break;
			}
			case 4: {
				System.out.println("return to my book info page");
				return; }

			default:
				System.out.println("잘못된 입력입니다.");

			}
		}

		/**
		 * @author ys.kim
		 * @param userName
		 * @param userRole
		 * 학생의 이름, 롤을 세션을 통해 받아와 동작하는 switch 문
		 * @throws SQLException 
		 */
		public void showStudent(String userName, String userRole) {
			// if member role == admin
			// else if member role == student
			System.out.println(
					"STUDENT MENU | 1. MY INFORMATION | 2. MY LIBRARY INFORMATION | 3. LOG OUT | 4. 도서 대출 | 5. 도서 반납");
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
			case 4: {
				System.out.println(userName + " user log out: " + userRole);
				loanSystem.loanBook();
//				Session.loggedInUser = null;
			}
			case 5:
				bookDao.returnBookById();
			default:
				System.out.println("시스템을 종료합니다.");

			}
		}

}
