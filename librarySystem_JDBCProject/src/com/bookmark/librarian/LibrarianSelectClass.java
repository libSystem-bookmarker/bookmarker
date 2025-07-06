package com.bookmark.librarian;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;
import com.bookmark.commondao.MemberCommonDAO;

public class LibrarianSelectClass {
	
	DataSource ds = new DataSource();
	LibrarianDAO librarianDAO = new LibrarianDAO();
	MemberCommonDAO mcDAO = new MemberCommonDAO();
	
	//librarian's select
			public void managingLibrarian() {
				System.out.println(
						Session.loggedInUser.getName()+"'s page | 1. MY INFORMATION | 2. UPDATE MY INFORMATION | 3. RETURN TO LIBRARIAN PAGE");
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
				case 3: {
					break;
				}
				case 4: {
					break;
				}
				case 5:
					System.out.println("return to my information page");
					return;

				default:
					System.out.println("잘못된 입력입니다.");

				}
			}

			public void managingBook() {
				
			}
	public void showLibrarian(String userName, String userRole) {
		// if member role == lib
			System.out.println("LIBRARIAN MENU | 1. MY INFORMATION | 2. BOOK MANAGING | 3. LOG OUT");
			int librarianMenu = Integer.parseInt(ds.sc.nextLine());
			switch (librarianMenu) {
			case 1: {
				managingLibrarian();
				break;
			}
			case 2: {
				managingBook();
				break;
			}
			case 3: {
				System.out.println(userName + " user log out: " + userRole);
				Session.loggedInUser = null;
				break;
			}
			default:
				System.out.println("잘못된 입력입니다.");

			}
		}

}
