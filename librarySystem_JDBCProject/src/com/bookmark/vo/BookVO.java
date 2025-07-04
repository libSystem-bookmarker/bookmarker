package com.bookmark.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class BookVO {
	
	private int bookId; //pk
	private int categoryId; //fk
	private String title; //제목
	private String author; //작가
	private String publisher; //출판사
	private int totalCount; //소장 책 수량
	private Date createAt; //출판일

}
