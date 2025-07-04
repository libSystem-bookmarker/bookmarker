package com.bookmark.vo;

import lombok.Data;

@Data
public class MemberVO {
	
	private int user_id; //pk
	private String pw; 
	private String role; //admin, student, librarian
	private String name; //name
	private String phone_number; //phone
	private String address; //address
	private int major_id; //fk
}
