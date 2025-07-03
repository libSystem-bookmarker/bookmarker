package com.bookmark.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class BookLoanDetailVO {
	
	private int bookLoanDetailId; //pk
	private int bookId; //fk
	private int userId; //fk
	private Date bookLoanDate; //대여일
	private Date bookReturnDate; //반납일

}
