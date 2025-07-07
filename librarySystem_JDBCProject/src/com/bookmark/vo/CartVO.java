package com.bookmark.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class CartVO {
	
	private int cart_id; //pk
	private int user_id; //fk
	private int book_id; //fk
	private int quantity;
	private Date added_date;
	
	

}
