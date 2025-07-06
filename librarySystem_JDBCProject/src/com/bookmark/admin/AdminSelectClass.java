package com.bookmark.admin;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;

public class AdminSelectClass {
	
	AdminDAO adminDAO = new AdminDAO();
	DataSource ds = new DataSource();
	
	// MEMBER switch
	public void managingMember() {

		System.out.println(
				"MEMBER MANAGER MENU | 1. INSERT MEMBER | 2. UPDATE MEMBER | 3. MEMBER LIST | 4. DELETE MEMBER | 5. RETURN TO ADMIN PAGE");
		int adminMenu = Integer.parseInt(ds.sc.nextLine());
			switch (adminMenu) {
			case 1: {
				adminDAO.insertMember();
				break;
			}
			case 2: {
				adminDAO.updateMember();
				break;
			}
			case 3: {
				System.out.println("MEMBER MANAGER LIST | 1. MEMBER LIST (ALL) | 2. ADMIN MEMBER LIST | 3. LIBRARIAN MEMBER LIST | 4. STUDENT LSIT |  5. RETURN TO ADMIN PAGE");
				int memberListMenu = Integer.parseInt(ds.sc.nextLine());
				switch (memberListMenu) {
				case 1:
					adminDAO.memberList();
					break;
				case 2:
					adminDAO.memberListFilteringAdmin();
					break;
				case 3:
					adminDAO.memberListFilteringLib();
					break;
				case 4:
					adminDAO.memberListFilteringStudent();
					break;
				case 5:
					System.out.println("return to admin main page");
					return;

				default:
					System.out.println("잘못된 입력입니다.");
					break;
				}
				break;
			}
			case 4: {
				adminDAO.deleteMember();
				break;
			}
			case 5: {
				System.out.println("return to admin main page");
				return;
			}
			default:
				System.out.println("잘못된 입력입니다.");

			}
		}

	// MAJOR switch
	public void managingMajor() {
		System.out.println(
				"MAJOR MANAGER MENU | 1. INSERT MAJOR | 2. UPDATE MAJOR | 3. MAJOR LIST | 4. DELETE MAJOR | 5. RETURN TO ADMIN PAGE");
		int adminMenu = Integer.parseInt(ds.sc.nextLine());
		switch (adminMenu) {
		case 1: {
			adminDAO.insertMajor();
			break;
		}
		case 2: {
			adminDAO.updateMajor();
			break;
		}
		case 3: {
			adminDAO.majorList();
			break;
		}
		case 4: {
			adminDAO.deletemajor();
			break;
		}
		case 5: {

			break;
		}
		default:
			System.out.println("잘못된 입력입니다.");

		}

	}

	public void showViewAdmin(String userName, String userRole) {
		// if member role == admin
		System.out.println("ADMIN MENU | 1. MEMBER MANAGER | 2. MAJOR MANAGER | 3. LOG OUT");
		int adminSelectMenu = Integer.parseInt(ds.sc.nextLine());

		switch (adminSelectMenu) {
		case 1:
			managingMember();
			break;
		case 2:
			managingMajor();
			break;
		case 3:
			System.out.println(userName + " user log out: " + userRole);
			Session.loggedInUser = null;
			break;

		default:
			System.out.println("잘못된 입력입니다.");
			break;
		}
	}

}
