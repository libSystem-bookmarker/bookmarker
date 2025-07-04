package com.bookmark.librarian;
import java.sql.Date;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bookmark.vo.BookVO;
import com.bookmark.vo.CategoryVO;

public class LibrarianMain {
	
	private static List<CategoryVO> categoryList = new ArrayList<>();
	private static List<BookVO> bookList = new ArrayList<>();
//	private static int lastBookId = 1;

	public static void main(String[] args) {
		
		LibrarianDAO dao = new LibrarianDAO();
		int lastBookId = dao.getLastBookId();
		System.out.println("lastBookId: " + lastBookId);

		Scanner scanner = new Scanner(System.in);
		
		System.out.println("\n📚 ================================");
		System.out.println("📖 새 책 등록을 시작합니다.");
		System.out.println("🖊️  아래 정보를 입력해 주세요.");
		System.out.println("==================================");

		
		BookVO book = new BookVO(++lastBookId);
		
		System.out.print("📕 책 제목          : ");
		book.setTitle(scanner.next());
		
		System.out.print("👤 작가             : ");
		book.setAuthor(scanner.next());

		System.out.print("🏢 출판사           : ");
		book.setPublisher(scanner.next());

		System.out.print("📅 출판일 (yyyy-MM-dd): ");
		book.setCreateAt(Date.valueOf(scanner.next()));  // java.sql.Date로 변환

		System.out.print("📦 소장 수량(권)    : ");
		book.setTotalCount(scanner.nextInt());
		
		System.out.println("📂 등록 가능한 카테고리 목록");
		System.out.println("-----------------------------------");
		System.out.println("  1. 문학");
		System.out.println("  2. 인문/사회");
		System.out.println("  3. 과학/기술");
		System.out.println("  4. 컴퓨터/IT");
		System.out.println("  5. 예술/디자인");
		System.out.println("  6. 역사");
		System.out.println("  7. 경제/경영");
		System.out.println("  8. 자기계발");
		System.out.println("  9. 여행");
		System.out.println(" 10. 취미/실용");
		System.out.println("-----------------------------------");
		System.out.print("📂 카테고리 ID를 선택하세요 (1~10): ");
		
		book.setCategoryId(scanner.nextInt());
		
		System.out.println(book);
		
		try {
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
			System.out.print("✅ 입력이 완료되었습니다. 책을 등록 중입니다 [");

			for (int i = 0; i < 20; i++) {
			    try {
			        Thread.sleep(200); // 0.15초 대기 (속도 조절 가능)
			    } catch (InterruptedException e) {
			        Thread.currentThread().interrupt();
			    }
			    System.out.print("■"); // 블록 출력
			    System.out.flush();     // 강제로 출력 (버퍼 비우기)
			}

			System.out.println("] 등록 완료!");
			
			// 방금 추가한 거면 마지막 index에만 new.

		}catch(RuntimeException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		
		
		
		categoryList = dao.getCategoryAll();
		System.out.println(categoryList);
		
		

	}

}
