package com.bookmark.admin;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;

public class AdminSelectClass {
	
	AdminDAO adminDAO = new AdminDAO();
	DataSource ds = new DataSource();
	
	// MEMBER switch
	/**
	 * @author ys.kim
	 * 회원 관리 switch 문
	 */
	public void managingMember() {

		System.out.println(
				"회원 관리 메뉴 | 1. 회원 생성 | 2.  회원 수정 | 3.회원 목록 | 4. 회원 삭제 | 0. 이전 페이지로 돌아가기");
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
				System.out.println("회원 목록 | 1. 회원 전체 조회 | 2. 관리자 회원 목록 | 3. 사서 회원 목록 | 4. 학생 회원 목록 |  0. 이전 페이지로 돌아가기");
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
				case 0:
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
			case 0: {
				System.out.println("return to admin main page");
				return;
			}
			default:
				System.out.println("잘못된 입력입니다.");

			}
		}

	// MAJOR switch
	/**
	 * @author ys.kim
	 * 전공 관리 switch 문
	 */
	public void managingMajor() {
		System.out.println(
				"학과 관리 | 1. 학과 생성 | 2. 학과 수정 | 3. 학과 목록 | 4. 학과 삭제 | 0. 이전 페이지로 돌아가기");
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
		case 0: {
			System.out.println("return to admin main page");
			return;
		}
		default:
			System.out.println("잘못된 입력입니다.");

		}

	}

	/**
	 * @author ys.kim
	 * @param userName
	 * @param userRole
	 * 현재 로그인한 사람의 정보를 받아와 switch 제어
	 */
	public void showViewAdmin(String userName, String userRole) {
		// if member role == admin
		System.out.println("관리자 | 1. 회원 관리 | 2. 학과 관리 | 0. LOG OUT");
		int adminSelectMenu = Integer.parseInt(ds.sc.nextLine());

		switch (adminSelectMenu) {
		case 1:
			managingMember();
			break;
		case 2:
			managingMajor();
			break;
		case 0:
			System.out.println(userName + " user log out: " + userRole);
			Session.loggedInUser = null;
			break;

		default:
			System.out.println("잘못된 입력입니다.");
			break;
		}
	}

}
