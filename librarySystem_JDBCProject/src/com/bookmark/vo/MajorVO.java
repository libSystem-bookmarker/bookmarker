package com.bookmark.vo;

import lombok.Data;

@Data
public class MajorVO {
	
	private int major_id; //pk
	private String major_name; //학부, 학과 이름
	private int parent_id; //parent_id == null -> 학부 / is not null == 학과

}
