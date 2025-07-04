package com.bookmark.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class BookWithCategoryVO {
	
	private int bookId; //pk
	private int categoryId; //fk
	private String categoryName; //카테고리 이름
	private String title; //제목
	private String author; //작가
	private String publisher; //출판사
	private int totalCount; //소장 책 수량
	private Date createAt; //출판일

	
	
	// 조회용 생성자
	public BookWithCategoryVO(int bookId, int categoryId, String categoryName, String title, String author,
			String publisher, int totalCount, Date createAt) {
		super();
		this.bookId = bookId;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.totalCount = totalCount;
		this.createAt = createAt;
	}



	public BookWithCategoryVO() {
		// TODO Auto-generated constructor stub
	}

}
