package com.bookmark.selectview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.bookmark.common.DataSource;
import com.bookmark.dao.BookDAO;
import com.bookmark.vo.BookVO;
import com.bookmark.vo.BookWithCategoryVO;

public class BookCRUDSelectClass {
	
	DataSource ds = new DataSource();
	private static BookDAO dao = new BookDAO();
	private static BookSelectClass view = new BookSelectClass();
	static ViewClass loginView = new ViewClass();

	/**
	 * @author yh.cha
	 * show book list
	 */
	public void selectBook() {
		List<BookWithCategoryVO> results = dao.getBookAll();
		System.out.println("수정할 도서 ID를 입력하세요.");

		// 결과 출력
		System.out.println("\n📘 도서 목록:");
		for (BookWithCategoryVO book : results) {
			System.out.println();
			System.out.printf("📘 [ID: %-3d] 📂 카테고리: %-2s[%d]\n", 
		            book.getBookId(),
		            book.getCategoryName(),
		            book.getCategoryId()
		            );

			System.out.printf("    📕 제목: %-20s | 👤 작가: %-5s | 🏢 출판사: %-5s\n", 
			                  book.getTitle(), 
			                  book.getAuthor(), 
			                  book.getPublisher());
			
			System.out.printf("    📅 출판일: %s  📦 수량: %d권\n", 
			                  new SimpleDateFormat("yyyy-MM-dd").format(book.getCreateAt()), 
			                  book.getTotalCount());
			System.out.println("----------------------------------------------------------------------------");
		}
	}

	/**
	 * @author yh.cha
	 * manageBook select
	 */
	public void manageBook() {

		BookDAO bookDAO = new BookDAO();

		while (true) {

			List<BookWithCategoryVO> searchResults = new ArrayList<>();

			System.out.println();
			System.out.println("\n📚 도서 관리");
			System.out.println("===========================================================================================");
			System.out.printf("%-10s %-10s %-10s %-10s %-10s \n", "1. 📖 도서 목록 조회 ", "2. 📝 도서 등록 ", "3. ✏️ 도서 수정 ", "4. ❌ 도서 삭제 ", "0. 🔚 도서 관리 종료 ");
			System.out.println("0. 프로그램 종료");
			System.out.print("▶▶ 메뉴 선택: ");

			int menu = Integer.parseInt(ds.sc.nextLine());

			switch (menu) {

			case 1:
				System.out.println("\n📚 도서 목록 조회");
				System.out.println("===========================================================================================");
				System.out.println("1. 전체 도서 목록 보기");
				System.out.println("2. 카테고리별 도서 목록 보기");
				System.out.println("3. 제목 또는 작가 검색");
				System.out.println("0. 조회 종료");
				System.out.print("▶▶(1~3): ");

				int num = Integer.parseInt(ds.sc.nextLine());

				switch (num) {
				case 1:
					searchResults = dao.getBookAll();
					break;
				case 2:
					System.out.println("\n📚 카테고리 선택");
					System.out.println("--------------------------------------------------");
					System.out.println("1. 총류  | 2. 철학  | 3. 종교  | 4. 사회과학  | 5. 자연과학");
					System.out.println("6. 기술과학  | 7. 예술  | 8. 언어  | 9. 문학  | 10. 역사");
					System.out.println("--------------------------------------------------");
					System.out.print("▶▶ 카테고리ID 입력: ");
					int categoryId = Integer.parseInt(ds.sc.nextLine());
					searchResults = dao.getBooksByCategory(categoryId);
					break;
				case 3:
					System.out.print("▶ 검색할 키워드를 입력하세요.(제목 또는 작가): ");
					String keyword = ds.sc.nextLine();
					searchResults = dao.getSearchBooks(keyword);
					break;
				case 0:
					System.out.println("도서 관리 메뉴로 돌아갑니다.");
//		                return;
					continue;
				default:
					System.out.println("❌ 잘못된 선택입니다.");
					return;
				}

				if (searchResults.isEmpty()) {
					System.out.println("📭 검색된 도서가 없습니다.");
					continue;
				} else {
					// 결과 출력
					System.out.println("\n                       도서 목록                                    ");
					System.out.println("=====================================================================");
					for (BookWithCategoryVO book : searchResults) {
						System.out.println();
						System.out.printf("📘 [ID: %-3d] 📂 카테고리: %-2s[%d]\n", 
					            book.getBookId(),
					            book.getCategoryName(),
					            book.getCategoryId()
					            );

						System.out.printf("    📕 제목: %-20s | 👤 작가: %-5s | 🏢 출판사: %-5s\n", 
						                  book.getTitle(), 
						                  book.getAuthor(), 
						                  book.getPublisher());
						
						System.out.printf("    📅 출판일: %s  📦 수량: %d권\n", 
						                  new SimpleDateFormat("yyyy-MM-dd").format(book.getCreateAt()), 
						                  book.getTotalCount());
						
						System.out.println("----------------------------------------------------------------------------");
					}

				}

				break;

			// 도서 등록
			case 2:
				try {

					BookVO book = view.inputInsertBook();
					dao.insertBook(book);

					System.out.println();
					System.out.println("========================================");
					System.out.printf("📖 책 ID         : %d\n", book.getBookId());
					System.out.printf("📕 제목          : %s\n", book.getTitle());
					System.out.printf("👩‍💼 작가          : %s\n", book.getAuthor());
					System.out.printf("🏢 출판사        : %s\n", book.getPublisher());
					System.out.printf("📅 출판일        : %s\n", book.getCreateAt().toString());
					System.out.printf("📦 총 권수       : %d\n", book.getTotalCount());
					System.out.printf("🗂 카테고리 ID    : %d\n", book.getCategoryId());
					System.out.println("========================================");
					System.out.println();
					System.out.print("✅ 입력이 완료되었습니다.");
					System.out.println("✅ 책을 등록 중입니다 [");

					for (int i = 0; i < 20; i++) {
						try {
							Thread.sleep(120); // 0.15초 대기 (속도 조절 가능)
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
						System.out.print("■"); // 블록 출력
						System.out.flush(); // 강제로 출력 (버퍼 비우기)
					}

					System.out.println("] 등록 완료!");

				} catch (IllegalArgumentException e) {
					System.out.println("❌ 입력 오류: " + e.getMessage());
				}
				break;

			// 도서 수정
			case 3:
				selectBook();
				

				System.out.print("도서 ID : ");
				int updateId = Integer.parseInt(ds.sc.nextLine());

				try {
					BookVO findBook = dao.getBookById(updateId);

					if (findBook == null) {
						break;
					}

					BookVO updateBook = view.inputUpdateBook(findBook);
					dao.updateBook(updateBook);

				} catch (RuntimeException e) {
					e.printStackTrace();
				}
				break;

			case 4:
				// 도서 삭제
				selectBook();

				System.out.print("🗑 삭제할 도서 ID를 입력하세요. ");
				System.out.println();
				System.out.print("▶▶ 도서 ID : ");
				int deleteId = Integer.parseInt(ds.sc.nextLine());

				try {
					dao.deleteBookById(deleteId);
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
				break;

			case 0:
				System.out.println("👋 도서 관리 시스템을 종료합니다.");
				return;

			default:
				System.out.println("❌ 잘못된 입력입니다.");

			}

		}

	}

}
