package com.bookmark.common;

import com.bookmark.vo.MemberVO;

/**
 * @author yeonsoo.kim
 * 로그인한 사용자의 role 판별을 위한 session
 */
public class Session {
	//로그인 객체 저장 -> 로그인한 대상의 role 판단 시 사용
	public static MemberVO loggedInUser = null;
}
