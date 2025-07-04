package com.bookmark.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class BookLoanDetailVO {
	
	private int bookLoanDetailId; //pk
	private int bookId; //fk
	private int userId; //fk
	private Date loanDate; //대여일
	private Date returnDate; //반납

}
