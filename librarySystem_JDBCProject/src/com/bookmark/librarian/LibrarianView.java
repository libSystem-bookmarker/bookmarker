package com.bookmark.librarian;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

import com.bookmark.vo.BookVO;

public class LibrarianView {
	
	private static LibrarianDAO dao = new LibrarianDAO();
	private static int lastBookId = 1;
	
	private Scanner scanner = new Scanner(System.in);
	
	
	// 도서 등록 및 입력 콘솔창
	public BookVO inputBook() {
		
		lastBookId = dao.getLastBookId();
		BookVO book = new BookVO(++lastBookId);
		System.out.println("lastBookId: " + lastBookId);
		
		
		System.out.println("\n📚 ================================");
		System.out.println("📖 새 책 등록을 시작합니다.");
		System.out.println("🖊️  아래 정보를 입력해 주세요.");
		System.out.println("==================================");
		
		
//		while(true) {
			try {
				
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
				
				return book;
				
			}catch(IllegalArgumentException e) {
				throw new IllegalArgumentException("❌ 잘못된 날짜 형식입니다. 다시 입력하세요. (예: 2025-07-04)");
//				System.out.println("❌ 잘못된 날짜 형식입니다. 다시 입력하세요. (예: 2025-07-04)");
			}catch(Exception e) {
				throw new RuntimeException(e);
			}finally {
				
			}
			
//		}
		
	}
	
}
