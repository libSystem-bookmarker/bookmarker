package com.bookmark.selectview;

import java.sql.SQLException;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;
import com.bookmark.dao.MemberCommonDAO;
import com.bookmark.dao.BookDAO;
import com.bookmark.dao.LoanDAO;
import com.bookmark.dao.CartDAO;
import com.bookmark.dao.StudentDAO;

public class StudentSelectClass {
	
	DataSource ds = new DataSource();
	StudentDAO studentDAO = new StudentDAO();
	BookDAO bookDao = new BookDAO();
	MemberCommonDAO mcDAO = new MemberCommonDAO();

	CartDAO cartDAO = new CartDAO();

	LoanDAO loanSystem = new LoanDAO();

	
		/**
		 * @author ys.kim
		 * 학생 정보 수정 및 조회하는 switch 문
		 */
		public void managingStudent() {
			
			System.out.println("📚 " + 
					Session.loggedInUser.getName()+"님 정보");
				System.out.println("============================================================");
				System.out.printf("%-20s %-20s %-10s\n", "1. 👤 내 정보 조회", "2. 📘 내 정보 수정", "0. 이전 페이지로");
				System.out.print("▶▶ ");
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
			case 0: {
				System.out.println("이전 페이지로 돌아갑니다.");
				return; }

			default:
				System.out.println("잘못된 입력입니다.");

			}
		}
		
		
		/**
		 * @author ys.kim
		 * 학생이 대여한 정보 조회 및 장바구니 기능 switch 문
		 * @throws SQLException 
		 */
		public void managingBookInfo() {
			
			//get user information
			int userId = Session.loggedInUser.getUser_id();
			
			System.out.println("\n\n");
			System.out.println(
					Session.loggedInUser.getName()+"님 > 도서 대출 정보");
			System.out.println("=================================================================");
			System.out.printf("%-20s %-20s %-10s\n", "1. 📖 대출 내역", " 2. 🛒 장바구니", "0. 이전 페이지");
			System.out.print("▶▶ ");
			int studentMenu = Integer.parseInt(ds.sc.nextLine());
			switch (studentMenu) {
			case 1: { studentDAO.loanedBookList(userId);
				break;
			}
			case 2: {  cartDAO.selectCartUserId(userId);
				break;
			}
			case 3: { 
				break;
			}
			case 0: {
				System.out.println("이전 페이지로 돌아갑니다.");
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
		 * 
		 * @author ys.kim, yh.cha
		 * modify: 2025.07.08
		 */
		public void showStudent(String userName, String userRole) {
			// if member role == admin
			// else if member role == student
			
			
			System.out.println("\n\n======================================================================================");
			System.out.printf("%-10s %-10s %-10s %-10s\n",
				    "1. 📖 도서 대출",
				    "2. 📥 도서 반납",
				    "3. 📚 나의 대출 정보",
				    "4. 🙋 마이 페이지  0. 🔓 로그아웃 "
				);
			System.out.print("▶▶ ");

			int studentMenu = Integer.parseInt(ds.sc.nextLine());
			switch (studentMenu) {
			case 1: {
				loanSystem.loanBook();
				break;
			}
			case 2: {
				bookDao.returnBookById();
				break;
			}
			case 3:  {
				managingBookInfo();
				break;
			}
			case 4: {
				managingStudent();		
				break;
			}
			case 0:
				System.out.println(userName + "[" + userRole + "]"+ "님 로그아웃 되었습니다.");
				Session.loggedInUser = null;
				break;
				
			default:
				System.out.println("시스템을 종료합니다.");

			}
		}

}
