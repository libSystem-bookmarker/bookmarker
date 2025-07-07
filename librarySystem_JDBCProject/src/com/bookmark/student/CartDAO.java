package com.bookmark.student;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookmark.common.DataSource;

public class CartDAO  {
Connection con = null;
DataSource ds = new DataSource();
CartVO cartVO = new CartVO();
    // 장바구니 추가
    public boolean insertCart(CartVO cart) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM cart WHERE user_id = ?";
        String insertSql = "INSERT INTO cart (cart_id, user_id, book_id) VALUES (cart_seq.NEXTVAL, ?, ?)";

        try (PreparedStatement countStmt = con.prepareStatement(countSql)) {
            countStmt.setInt(1, cart.getUser_id());
            ResultSet rs = countStmt.executeQuery();
            if (rs.next() && rs.getInt(1) >= 5) {
                return false; 
            }
        }

        try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
            pstmt.setInt(1, cart.getUser_id());
            pstmt.setInt(2, cart.getBook_id());
            return pstmt.executeUpdate() > 0;
        }
    }

    // 장바구니 도서 삭제
    public boolean deleteByBookId(int userId, int bookId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ? AND book_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // 장바구니 조회
    public List<CartVO> getCartByUserId(int userId) throws SQLException {
        List<CartVO> list = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE user_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                CartVO dto = new CartVO();
                dto.setCart_id(rs.getInt("cart_id"));
                dto.setUser_id(rs.getInt("user_id"));
                dto.setBook_id(rs.getInt("book_id"));
                list.add(dto);
            }
        }
        return list;
    }
}