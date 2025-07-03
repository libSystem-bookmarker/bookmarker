package com.bookmark.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class BookVO {
	
	private int bookId; //pk
	private int categoryId; //fk
	private String bookTitle; //제목
	private String bookAuthor; //작가
	private String bookPublisher; //출판사
	private int bookTotalCount; //소장 책 수량
	private Date bookCreateAt; //출판일

}
