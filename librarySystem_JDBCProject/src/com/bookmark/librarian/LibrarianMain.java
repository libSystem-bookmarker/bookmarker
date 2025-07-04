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
	

	public static void main(String[] args) {
		
		LibrarianView view = new LibrarianView();
		LibrarianDAO dao = new LibrarianDAO();
		
				try {
			
			BookVO book = view.inputBook();			dao.insertBook(book);						System.out.println();			System.out.println("========================================");			System.out.printf("📖 책 ID         : %d\n", book.getBookId());			System.out.printf("📕 제목          : %s\n", book.getTitle());
			System.out.printf("👩‍💼 작가          : %s\n", book.getAuthor());
			System.out.printf("🏢 출판사        : %s\n", book.getPublisher());
			System.out.printf("📅 출판일        : %s\n", book.getCreateAt().toString());
			System.out.printf("📦 총 권수       : %d\n", book.getTotalCount());
			System.out.printf("🗂 카테고리 ID    : %d\n", book.getCategoryId());			System.out.println("========================================");
			System.out.println();
			System.out.print("✅ 입력이 완료되었습니다. 책을 등록 중입니다 [");

			for (int i = 0; i < 20; i++) {
			    try {
			        Thread.sleep(200); // 0.15초 대기 (속도 조절 가능)			    } catch (InterruptedException e) {
			        Thread.currentThread().interrupt();
			    }
			    System.out.print("■"); // 블록 출력
			    System.out.flush();     // 강제로 출력 (버퍼 비우기)
			}

			System.out.println("] 등록 완료!");
			
			// 방금 추가한 거면 마지막 index에만 new.

		} catch (IllegalArgumentException e) {
		    System.out.println("❌ 입력 오류: " + e.getMessage());
		}

		catch(RuntimeException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		
		
		
		categoryList = dao.getCategoryAll();
		System.out.println(categoryList);
		
		

	}

}
