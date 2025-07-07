package com.bookmark.student;
import lombok.Data;
@Data
public class CartVO {
	private int cart_id;
	private int user_id;
	private int book_id;
	private int quantity;
	private java.sql.Date added_date;
}