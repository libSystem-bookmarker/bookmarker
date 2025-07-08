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
	private Date dueDate; // 실제 반납일
	private String isOverdue; // 연체여부
	private String isPenalized; // 연체 패널티
	
	
	
	public BookLoanDetailVO(int bookId, int userId, Date loanDate, Date returnDate) {
		this.bookId = bookId;
		this.userId = userId;
		this.loanDate = loanDate;
		this.returnDate = returnDate;
	}

	
	
}
