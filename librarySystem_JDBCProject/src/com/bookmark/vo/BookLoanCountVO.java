package com.bookmark.vo;

import lombok.Data;

@Data
public class BookLoanCountVO {
	
	private int bookLoanCountId; //pk
	private int userId; //fk
	private int bookLoanCount; //빌린 횟수
	

}
