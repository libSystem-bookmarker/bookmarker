package com.bookmark.librarian;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bookmark.vo.BookVO;
import com.bookmark.vo.BookWithCategoryVO;
import com.bookmark.vo.CategoryVO;

public class BookView {
	
	private BookDAO dao = new BookDAO();
	private int lastBookId = 1;
	
	Scanner scanner = new Scanner(System.in);
	
	
//	// 도서 전체 목록 보기 콘솔
//	public void showBookList() {
//		
//		List<BookWithCategoryVO> bookList = dao.getBookAll();
//		
//		System.out.println();
//	    System.out.println("📚 등록된 도서 목록");
//	    System.out.println("==========================================================================================================");
//	    System.out.printf(" %-4s  %-10s  %-15s  %-10s  %-10s  %-5s  %-12s  %-10s\n",
//	            "ID", "카테고리", "제목", "작가", "출판사", "수량", "출판일", "카테고리ID");
//	    System.out.println("----------------------------------------------------------------------------------------------------------");
//
//	    for (BookWithCategoryVO book : bookList) {
//	        System.out.printf(" %-4d  %-10s  %-15s  %-10s  %-10s  %-5d  %-12s  %-10d\n",
//	                book.getBookId(),
//	                book.getCategoryName(),
//	                book.getTitle(),
//	                book.getAuthor(),
//	                book.getPublisher(),
//	                book.getTotalCount(),
//	                book.getCreateAt().toString(),
//	                book.getCategoryId());
//	    }
//
//	    System.out.println("==========================================================================================================");
//		}
	
	
	
	// 카테고리로 도서 조회 콘솔
	public void inputCategoryBook() {
		// 카테고리에 따른 도서 조회
				System.out.println("📚 열람하고 싶은 도서의 카테고리 ID를 입력하세요.");
				System.out.println("--------------------------------------------------");
				System.out.println("1. 총류  | 2. 철학  | 3. 종교  | 4. 사회과학  | 5. 자연과학");
				System.out.println("6. 기술과학  | 7. 예술  | 8. 언어  | 9. 문학  | 10. 역사");
				System.out.println("--------------------------------------------------");
				System.out.print("▶ 카테고리 ID 입력: ");

				int categoryId = scanner.nextInt();
				
				List<BookWithCategoryVO> bookList = new ArrayList<>();
				
				try {
					bookList = dao.getBooksByCategory(categoryId);
				}catch(RuntimeException e) {
					System.out.println("❌ 도서 조회 중 오류 발생: " + e.getMessage());
					return;
				}
				
				if (bookList.isEmpty()) {
			        System.out.println("📭 해당 카테고리에 등록된 도서가 없습니다.");
			        return;
			    }
					
			    System.out.println("\n📚 해당 카테고리의 도서 목록:");
			    System.out.println("----------------------------------------------------------------------------------------------------------");
			    for (BookWithCategoryVO book : bookList) {
			        System.out.printf("📘 [ID:%d] [카테고리: %s[%d] ] 제목: %s | 작가: %s | 출판사: %s | 출판일: %s | 수량 : %d권\n",
			                book.getBookId(), 
			                book.getCategoryName(),
			                book.getCategoryId(),
			                book.getTitle(),
			                book.getAuthor(),
			                book.getPublisher(),
			                book.getCreateAt().toString(),
			                book.getTotalCount()
			        );
			    }

			    System.out.println("----------------------------------------------------------------------------------------------------------");
	}
	
	
	
	// 도서 검색 콘솔
	public void inputSearchBook() {
		
		// 도서 검색
		System.out.print("🔎 검색할 키워드를 입력하세요.(제목 또는 작가): ");
		String keyword = scanner.nextLine();

		List<BookWithCategoryVO> results = dao.getSearchBooks(keyword);

		if (results.isEmpty()) {
		    System.out.println("📭 검색 결과가 없습니다.");
		} else {
		    System.out.println("\n🔍 검색 결과:");
		    for (BookWithCategoryVO book : results) {
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
		}
		
	}
		
		
	
	
	
	// 도서 등록 및 입력 콘솔
	public BookVO inputInsertBook() {
		
		lastBookId = dao.getLastBookId();
		BookVO book = new BookVO(++lastBookId);
		System.out.println("lastBookId: " + lastBookId);
		
		List<CategoryVO> categoryList = dao.getCategoryAll();
		
		
		System.out.println("\n📚 ================================");
		System.out.println("📖 새 책 등록을 시작합니다.");
		System.out.println("🖊️  아래 정보를 입력해 주세요.");
		System.out.println("==================================");
		
		
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
				
				
				for(int i = 0; i < categoryList.size(); i++) {
					System.out.println("  " + categoryList.get(i).getCategoryId() + ". " + categoryList.get(i).getName());
				}
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
	}
			
			
			
			/* 도서 수정 콘솔 */
			public BookVO inputUpdateBook(BookVO book) {
			    while (true) {
			        System.out.println("\n📘 수정할 항목을 선택하세요 (0 입력 시 수정 종료)");
			        System.out.println("1. 제목");
			        System.out.println("2. 작가");
			        System.out.println("3. 출판사");
			        System.out.println("4. 출판일");
			        System.out.println("5. 소장 수량");
			        System.out.println("6. 카테고리 ID");
			        System.out.print("선택 ▶ ");
			        
			        if (!scanner.hasNextInt()) {
			            System.out.println("❌ 숫자를 입력해 주세요.");
			            scanner.nextLine(); // 버퍼 비우기
			            continue;
			        }

			        int num = scanner.nextInt();
			        scanner.nextLine(); // 버퍼 정리
			        
			        switch (num) {
			            case 0:
			                System.out.println("✅ 수정이 완료되었습니다.");
			                return book;

			            case 1:
			                System.out.print("📕 새 제목: ");
			                book.setTitle(scanner.nextLine());
			                break;

			            case 2:
			                System.out.print("👤 새 작가: ");
			                book.setAuthor(scanner.nextLine());
			                break;

			            case 3:
			                System.out.print("🏢 새 출판사: ");
			                book.setPublisher(scanner.nextLine());
			                break;

			            case 4:
			                System.out.print("📅 새 출판일 (yyyy-MM-dd): ");
			                try {
			                    book.setCreateAt(Date.valueOf(scanner.nextLine()));
			                } catch (IllegalArgumentException e) {
			                    System.out.println("❌ 잘못된 날짜 형식입니다. 다시 입력하세요.");
			                }
			                break;

			            case 5:
			                System.out.print("📦 새 소장 수량: ");
			                if (scanner.hasNextInt()) {
			                    book.setTotalCount(scanner.nextInt());
			                    scanner.nextLine();
			                } else {
			                    System.out.println("❌ 숫자를 입력해 주세요.");
			                    scanner.nextLine(); // 잘못된 입력 버림
			                }
			                break;

			            case 6:
			                System.out.print("📂 새 카테고리 ID (1~10): ");
			                if (scanner.hasNextInt()) {
			                    book.setCategoryId(scanner.nextInt());
			                    scanner.nextLine();
			                } else {
			                    System.out.println("❌ 숫자를 입력해 주세요.");
			                    scanner.nextLine(); // 잘못된 입력 버림
			                }
			                break;
			            default:
			                System.out.println("❌ 올바른 메뉴 번호를 선택하세요.");
			        }

			        System.out.println("🔄 현재 수정된 도서 정보:");
			        System.out.printf("  📕 제목: %s | 👤 작가: %s | 🏢 출판사: %s\n", book.getTitle(), book.getAuthor(), book.getPublisher());
			        System.out.printf("  📅 출판일: %s | 📦 수량: %d | 📂 카테고리 ID: %d\n", 
			                          book.getCreateAt(), book.getTotalCount(), book.getCategoryId());
			    }
			}

			
		
	}

