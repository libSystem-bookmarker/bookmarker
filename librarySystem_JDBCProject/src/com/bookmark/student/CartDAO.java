package com.bookmark.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookmark.common.DataSource;

public class CartDAO {
    private DataSource ds = new DataSource();

    // 장바구니 추가
    public boolean insertCart(CartVO cart) {
        String countSql = "SELECT COUNT(*) FROM cart WHERE user_id = ?";
        String insertSql = "INSERT INTO cart (cart_id, user_id, book_id, quantity) VALUES (cart_seq.NEXTVAL, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement countStmt = con.prepareStatement(countSql)) {

            countStmt.setInt(1, cart.getUser_id());
            ResultSet rs = countStmt.executeQuery();
            if (rs.next() && rs.getInt(1) >= 5) {
                // 이미 5권 이상 담았으면 추가 불가
                return false;
            }

            try (PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
                insertStmt.setInt(1, cart.getUser_id());
                insertStmt.setInt(2, cart.getBook_id());
                insertStmt.setInt(3, cart.getQuantity());
                return insertStmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 장바구니 도서 삭제
    public boolean deleteByBookId(int userId, int bookId) {
        String sql = "DELETE FROM cart WHERE user_id = ? AND book_id = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 장바구니 조회
    public List<CartVO> getCartByUserId(int userId) {
        List<CartVO> list = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE user_id = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                CartVO dto = new CartVO();
                dto.setCart_id(rs.getInt("cart_id"));
                dto.setUser_id(rs.getInt("user_id"));
                dto.setBook_id(rs.getInt("book_id"));
                dto.setQuantity(rs.getInt("quantity"));
                dto.setAdded_date(rs.getDate("added_date"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}