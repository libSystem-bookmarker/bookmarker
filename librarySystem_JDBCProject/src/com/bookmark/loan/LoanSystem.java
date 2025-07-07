package com.bookmark.loan;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bookmark.librarian.BookDAO;
import com.bookmark.librarian.BookView;
import com.bookmark.vo.BookWithCategoryVO;

public class LoanSystem {
	
	// 전역
	private static Scanner sc = new Scanner(System.in);
	private static BookDAO dao = new BookDAO();
	private static BookView view = new BookView();
	
	public static void main(String[] args) {
		
		
		
		while(true) {
			
		        
		        List<BookWithCategoryVO> searchResults = new ArrayList<>();

		        System.out.println("\n📚 도서 대출을 시작합니다.");
		        System.out.println("1. 전체 도서 목록 보기");
		        System.out.println("2. 카테고리별 도서 목록 보기");
		        System.out.println("3. 제목 또는 작가 검색");
		        System.out.print("👉 원하는 방법을 선택하세요 (1~3): ");

		        int num = sc.nextInt();
		        sc.nextLine(); // 개행 버퍼 제거

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
		                int categoryId = sc.nextInt();
		                sc.nextLine();
		                searchResults = dao.getBooksByCategory(categoryId);
		                break;
		            case 3:
		                System.out.print("▶ 검색할 키워드를 입력하세요.(제목 또는 작가): ");
		                String keyword = sc.nextLine();
		                searchResults = dao.getSearchBooks(keyword);
		                break;
		            default:
		                System.out.println("❌ 잘못된 선택입니다.");
		                return;
		        }

		        if (searchResults.isEmpty()) {
		            System.out.println("📭 검색된 도서가 없습니다.");
		            return;
		        }

		        // 결과 출력
		        System.out.println("\n📘 도서 목록:");
		        for (BookWithCategoryVO book : searchResults) {
		            System.out.printf("📘 [ID: %d] [카테고리: %d - %s] 제목: %s | 작가: %s | 출판사: %s | 출판일: %s | 수량: %d권\n",
		                book.getBookId(),
		                book.getCategoryId(),
		                book.getCategoryName(),
		                book.getTitle(),
		                book.getAuthor(),
		                book.getPublisher(),
		                book.getCreateAt().toString(),
		                book.getTotalCount()
		            );
		        }
		        
		        
		        
		        System.out.print("\n📌 대출할 도서의 ID를 입력하세요 (0: 취소): ");
		        int bookId = sc.nextInt();

		        if (bookId == 0) {
		            System.out.println("❎ 도서 대출이 취소되었습니다.");
		            break;
		        }
		        
		        
		}

	}

}
