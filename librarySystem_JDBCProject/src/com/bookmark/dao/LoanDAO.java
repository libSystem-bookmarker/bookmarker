package com.bookmark.dao;

import java.util.ArrayList;
import java.util.List;

import com.bookmark.common.DataSource;
import com.bookmark.common.Session;

import com.bookmark.vo.BookWithCategoryVO;

public class LoanDAO {

	// 조회
	BookDAO dao = new BookDAO();
//	private static BookSelectClass view = new BookSelectClass();
//	static ViewClass loginView = new ViewClass();
	DataSource ds = new DataSource();
	CartDAO cartDAO = new CartDAO();  //장바구니

	/**
	 * 도서 대출 시스템
	 * 
	 * @author yunha 1. 도서 목록 조회(전체, 카테고리, 검색) 2. 도서 목록 조회 후 insertLoanBook() 메서드로
	 *         선택한 도서 대출
	 */
	public void loanBook() {
		while (true) {
			List<BookWithCategoryVO> searchResults = new ArrayList<>();

			System.out.println("\n📚 도서 대출");
			System.out.println("1. 전체 도서 목록 보기");
			System.out.println("2. 카테고리별 도서 목록 보기");
			System.out.println("3. 제목 또는 작가 검색");
			System.out.println("0. 선택 시 종료");
			System.out.print("▶▶ (1~3): ");

			int num = ds.sc.nextInt();
			ds.sc.nextLine(); // 개행 버퍼 제거

			switch (num) {
			case 1:
				searchResults = dao.getBookAll();
				break;
			case 2:
				System.out.println("--------------------------------------------------");
				System.out.println("1. 총류  | 2. 철학  | 3. 종교  | 4. 사회과학  | 5. 자연과학");
				System.out.println("6. 기술과학  | 7. 예술  | 8. 언어  | 9. 문학  | 10. 역사");
				System.out.println("--------------------------------------------------");
				System.out.print("▶ 카테고리 ID 입력: ");
				int categoryId = ds.sc.nextInt();
				ds.sc.nextLine();
				searchResults = dao.getBooksByCategory(categoryId);
				break;
			case 3:
				System.out.print("▶ 검색할 키워드를 입력하세요.(제목 또는 작가): ");
				String keyword = ds.sc.nextLine();
				searchResults = dao.getSearchBooks(keyword);
				break;
			case 0:
				System.out.println("대출 시스템을 종료합니다.");
				return;
			default:
				System.out.println("❌ 잘못된 선택입니다.");
				return;
			}

			if (searchResults.isEmpty()) {
				System.out.println("📭 검색된 도서가 없습니다.");
				continue;
			} else {
				// 결과 출력
				System.out.println("\n📘 도서 목록:");
				for (BookWithCategoryVO book : searchResults) {
					System.out.printf("📘 [ID: %d] [카테고리: %d - %s] 제목: %s | 작가: %s | 출판사: %s | 출판일: %s | 수량: %d권\n",
							book.getBookId(), book.getCategoryId(), book.getCategoryName(), book.getTitle(),
							book.getAuthor(), book.getPublisher(), book.getCreateAt().toString(), book.getTotalCount());
				}

			}

			System.out.println("1. 장바구니에 담기 | 2. 대출하기");
			int selectCartOrLoan = Integer.parseInt(ds.sc.nextLine());
			switch (selectCartOrLoan) {
			case 1:
				System.out.print("\n📌 장바구니에 담을 도서의 ID를 입력하세요 (0: 이전 도서 목록으로): ");
				int bookId = Integer.parseInt(ds.sc.nextLine());
				if (bookId == 0) {
					System.out.println("❎ 장바구니에 도서 담기가 취소되었습니다.");
					continue;
				} else {

					int userId = Session.loggedInUser.getUser_id(); // 로그인 사용자 id 라고 가정
					//장바구니에 담기
					cartDAO.insertCart(userId, bookId);

				} //

				break;
			case 2:

				System.out.print("\n📌 대출할 도서의 ID를 입력하세요 (0: 이전 도서 목록으로): ");
				bookId = Integer.parseInt(ds.sc.nextLine());
				if (bookId == 0) {
					System.out.println("❎ 도서 대출이 취소되었습니다.");
					continue;
				} else {

					int userId = Session.loggedInUser.getUser_id(); // 로그인 사용자 id 라고 가정

					// 대출하기
					dao.insertLoanBook(bookId, userId);

				}
				break;
			default: System.out.println("잘못된 입력입니다.");
				break;
			}

		}

	}

}
