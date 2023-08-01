package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.dto.jwt.TokenResponseDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    /*회원가입*/
    MemberResponseDto signup(MemberRequestDto requestDto);

    /*로그인*/
    TokenResponseDto login(MemberRequestDto requestDto);

    /*회원 상세정보*/
    MemberResponseDto findMemberDetail(String email);

    /*회원정보 수정*/
    MemberResponseDto updateMember(String email, MemberRequestDto requestDto);

    /*회원탈퇴*/
    void deleteMember(String email);

    /*이메일 중복검사*/
    boolean checkEmailDuplicate(String email);

    /*닉네임 중복검사*/
    boolean checkUsernameDuplicate(String username);

    void logout(String email);
}
