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
	
	
	 // 목록 조회용 생성자
    public BookVO(int bookId, int categoryId, String title, String author,
                  String publisher, int totalCount, Date createAt) {
        this.bookId = bookId;
        this.categoryId = categoryId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.totalCount = totalCount;
        this.createAt = createAt;
    }
    
    // 상세 조회용 생성자
    
    
//    bookId만 받는 생성자
    public BookVO(int bookId) {
    	this.bookId = bookId;
    }

	public BookVO() {
		// TODO Auto-generated constructor stub
	}

}
