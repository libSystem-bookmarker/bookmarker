package com.bookmark.librarian;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;
import com.bookmark.commondao.MemberCommonDAO;
import com.bookmark.loan.LoanSystem;
import com.bookmark.vo.BookWithCategoryVO;

public class LibrarianSelectClass {
	
	DataSource ds = new DataSource();
	BookDAO bookDao = new BookDAO();
	MemberCommonDAO mcDAO = new MemberCommonDAO();
	
	BookCRUD bookManage = new BookCRUD();

	
	//librarian's select
			public void managingLibrarian() {
				System.out.println(
						Session.loggedInUser.getName()+"'s page | 1. 나의 정보 | 2. 나의 정보 수정 | 0. 이전 페이지로 돌아가기");
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
					System.out.println("return to my information page");
					return;

				default:
					System.out.println("잘못된 입력입니다.");

				}
			}


			
	public void showLibrarian(String userName, String userRole) {
		// if member role == lib
			System.out.println("도서관 사서 메뉴 | 1. 내 정보 관리 | 2. 도서 관리 | 0. 로그아웃");
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
				System.out.println(userName + " user log out: " + userRole);

				Session.loggedInUser = null;
				break;
			}
			default:
				System.out.println("시스템을 종료합니다.");

			}
		}

}
