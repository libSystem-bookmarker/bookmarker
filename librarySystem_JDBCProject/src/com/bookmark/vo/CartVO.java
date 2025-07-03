package com.bookmark.vo;

import lombok.Data;

@Data
public class CartVO {
	
	private int cartId; //pk
	private int userId; //fk
	private int bootId; //fk
	

}
