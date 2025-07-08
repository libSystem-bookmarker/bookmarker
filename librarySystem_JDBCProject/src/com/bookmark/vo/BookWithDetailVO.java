package com.bookmark.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class BookWithDetailVO {
	
	private int bookId;
	private String title; // 제목
	private String author; //작가
	private String publisher; //출판사
	private int userId; //fk

	private Date loanDate; //대여일
	private Date returnDate; //반납
	private Date dueDate; // 실제 반납일
	private String isOverdue; // 연체여부


}
