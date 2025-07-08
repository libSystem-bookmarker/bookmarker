package com.bookmark.selectview;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;
import com.bookmark.dao.MemberCommonDAO;
import com.bookmark.dao.BookDAO;
import com.bookmark.dao.LoanDAO;
import com.bookmark.vo.BookWithCategoryVO;

public class LibrarianSelectClass {
	
	DataSource ds = new DataSource();
	BookDAO bookDao = new BookDAO();
	MemberCommonDAO mcDAO = new MemberCommonDAO();
	
	BookCRUDSelectClass bookManage = new BookCRUDSelectClass();

	
	//librarian's select
			public void managingLibrarian() {
				System.out.println(
					Session.loggedInUser.getName()+"\n📚님 환영합니다!");
				
				System.out.println("============================================================");
				System.out.printf("%-20s %-20s %-10s\n", "1. 👤 내 정보 조회", "2. 📘 내 정보 수정", "0. 이전 페이지로 ");
				System.out.print("▶▶ ");
				
				int librarianMenu = Integer.parseInt(ds.sc.nextLine());
				switch (librarianMenu) {
				case 1: {
					mcDAO.userDetails();
					break;
				}
				case 2: {
					mcDAO.updateMember();
					break;
				}
				case 0:
					System.out.println("이전 페이지로 돌아갑니다.");
					return;

				default:
					System.out.println("잘못된 입력입니다.");

				}
			}


			
	public void showLibrarian(String userName, String userRole) {
		// if member role == lib
			System.out.println();
			System.out.println("		       도서관 사서 메뉴							");
			System.out.println("============================================================");
			System.out.printf("%-20s %-20s %-10s\n", "1. 👤 내 정보 관리", "2. 📘 도서 관리", "0. 🔒 로그아웃");
			System.out.print("▶▶ ");
			int librarianMenu = Integer.parseInt(ds.sc.nextLine());
			switch (librarianMenu) {
			case 1: {
				managingLibrarian();
				break;
			}
			case 2: {
				bookManage.manageBook();
				break;
			}
			case 0: {
				System.out.println(userName + "[" + userRole + "]"+ "님 로그아웃 되었습니다.");
				Session.loggedInUser = null;
				break;
			}
			default:
				System.out.println("시스템을 종료합니다.");

			}
		}

}
