package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.dto.jwt.TokenResponseDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberResponseDto;
import com.HowBaChu.howbachu.domain.dto.member.StatusResponseDto;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MemberService {

    /*회원가입*/
    MemberResponseDto signup(MemberRequestDto.signup requestDto);

    /*로그인*/
    TokenResponseDto login(MemberRequestDto.login requestDto, HttpServletResponse response);

    /*회원 상세정보*/
    MemberResponseDto findMemberDetail(String email);

    /*회원정보 수정*/
    MemberResponseDto updateMember(String email, MemberRequestDto.update requestDto);

    /*회원탈퇴*/
    void deleteMember(String email);

    /*이메일 중복검사*/
    StatusResponseDto checkEmailDuplicate(String email);

    /*닉네임 중복검사*/
    StatusResponseDto checkUsernameDuplicate(String username);

    void logout(String email, HttpServletResponse response);

    MemberResponseDto uploadAvatar(String email, MultipartFile image);

    void deleteAvatar(String email);

    StatusResponseDto checkPassword(String password, String email);
}
