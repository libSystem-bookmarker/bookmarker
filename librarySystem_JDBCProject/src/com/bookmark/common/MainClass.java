package com.bookmark.common;

import java.util.List;
import java.util.Scanner;

import com.bookmark.student.CartDAO;
import com.bookmark.student.CartVO;

/**
 * @author 김병석
 * @desc 콘솔 기반 장바구니 관리 시스템 메인 클래스
 */
public class MainClass {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CartDAO cartDAO = new CartDAO(); // DAO 내부에서 DB 연결 관리

        while (true) {
            System.out.println("\n===== 장바구니 시스템 =====");
            System.out.println("1. 도서 추가");
            System.out.println("2. 도서 삭제");
            System.out.println("3. 내 장바구니 보기");
            System.out.println("4. 종료");
            System.out.print("메뉴 선택: ");

            int menu = -1;
            try {
                menu = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("숫자로 입력해주세요.");
                continue;
            }

            try {
                switch (menu) {
                    case 1: {
                        System.out.print("user_id 입력: ");
                        int userId = Integer.parseInt(sc.nextLine());
                        System.out.print("book_id 입력: ");
                        int bookId = Integer.parseInt(sc.nextLine());

                        CartVO cart = new CartVO();
                        cart.setUser_id(userId);
                        cart.setBook_id(bookId);
                        cart.setQuantity(1);  // 기본 수량 1로 세팅

                        boolean inserted = cartDAO.insertCart(cart);
                        System.out.println(inserted ? "✅ 도서 추가 완료" : "⚠️ 도서 5권 초과로 추가 불가");
                        break;
                    }

                    case 2: {
                        System.out.print("user_id 입력: ");
                        int delUserId = Integer.parseInt(sc.nextLine());
                        System.out.print("book_id 입력: ");
                        int delBookId = Integer.parseInt(sc.nextLine());

                        boolean deleted = cartDAO.deleteByBookId(delUserId, delBookId);
                        System.out.println(deleted ? "🗑️ 도서 삭제 완료" : "❌ 삭제 실패 (존재하지 않음)");
                        break;
                    }

                    case 3: {
                        System.out.print("user_id 입력: ");
                        int viewUserId = Integer.parseInt(sc.nextLine());

                        List<CartVO> cartList = cartDAO.getCartByUserId(viewUserId);
                        if (cartList.isEmpty()) {
                            System.out.println("🛒 장바구니가 비어있습니다.");
                        } else {
                            System.out.println("📚 내 장바구니 목록:");
                            for (CartVO c : cartList) {
                                System.out.printf("Cart ID: %d | Book ID: %d | 수량: %d | 추가일: %s\n",
                                    c.getCart_id(), c.getBook_id(), c.getQuantity(), c.getAdded_date());
                            }
                        }
                        break;
                    }

                    case 4:
                        System.out.println("👋 프로그램을 종료합니다.");
                        sc.close();
                        return;

                    default:
                        System.out.println("⚠️ 잘못된 메뉴 번호입니다. 다시 입력해주세요.");
                }
            } catch (Exception e) {
                System.out.println("🚨 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

