package com.bookmark.vo;

import lombok.Data;

@Data
public class CategoryVO {
	
	private int categoryId; //pk
	private String name; //카테고리명
	
	
	public CategoryVO(int categoryId, String name) {
		this.categoryId = categoryId;
		this.name = name;
	}
	
	

}
