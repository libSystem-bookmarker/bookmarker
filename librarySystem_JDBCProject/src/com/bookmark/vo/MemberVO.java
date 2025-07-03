package com.bookmark.vo;

import lombok.Data;

@Data
public class MemberVO {
	
	private int userId; //pk
	private String pw; 
	private String role; //admin, student, librarian
	private String userName; //name
	private String phoneNumber; //phone
	private String address; //address
	private int major_id; //fk
}
