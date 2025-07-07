package com.bookmark.student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Scanner;

import com.bookmark.student.CartDAO;
import com.bookmark.student.CartVO;

public class CartSystemMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);



            while (true) {
                System.out.println("장바구니 메뉴: 1. 도서 추가 | 2. 도서 삭제 | 3. 내 장바구니 보기 | 4. 종료");
                int menu = Integer.parseInt(sc.nextLine());

                switch (menu) {
                    case 1:
                        System.out.print("user_id 입력: ");
                        int userId = Integer.parseInt(sc.nextLine());

                        System.out.print("book_id 입력: ");
                        int bookId = Integer.parseInt(sc.nextLine());

                        CartVO cart = new CartVO();
                        cart.setUser_id(userId);
                        cart.setBook_id(bookId);

                        boolean inserted = dao.insertCart(cart);
                        System.out.println(inserted ? "추가 완료" : "5권 초과로 추가 불가");
                        break;

                    case 2:
                        System.out.print("user_id 입력: ");
                        int delUserId = Integer.parseInt(sc.nextLine());

                        System.out.print("book_id 입력: ");
                        int delBookId = Integer.parseInt(sc.nextLine());

                        boolean deleted = dao.deleteByBookId(delUserId, delBookId);
                        System.out.println(deleted ? "삭제 완료" : "삭제 실패");
                        break;

                    case 3:
                        System.out.print("user_id 입력: ");
                        int viewUserId = Integer.parseInt(sc.nextLine());

                        List<CartVO> cartList = dao.getCartByUserId(viewUserId);
                        if (cartList.isEmpty()) {
                            System.out.println("장바구니가 비어 있습니다.");
                        } else {
                            for (CartVO c : cartList) {
                                System.out.printf("Cart ID: %d | Book ID: %d\n", c.getCart_id(), c.getBook_id());
                            }
                        }
                        break;

                    case 4:
                        System.out.println("프로그램 종료");
                        sc.close();
                        return;

                    default:
                        System.out.println("잘못된 메뉴입니다.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}