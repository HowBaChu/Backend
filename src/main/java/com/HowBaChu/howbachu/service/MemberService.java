package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberResponseDto;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    /*회원가입*/
    MemberResponseDto signup(MemberRequestDto requestDto);

    /*로그인*/
    MemberResponseDto login(MemberRequestDto requestDto, HttpServletResponse response);

    /*회원검색*/
    void findMember(String email);

    /*회원정보 수정*/
    void updateMember(String email, MemberRequestDto requestDto);

    /*회원탈퇴*/
    void deleteMember(String email, MemberRequestDto requestDto);
}
