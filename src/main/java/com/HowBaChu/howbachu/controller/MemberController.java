package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.response.ResResult;
import com.HowBaChu.howbachu.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/members")
public class MemberController {

    private final MemberService memberService;

    /*회원가입*/
    @PostMapping("/signup")
    public ResponseEntity<ResResult> register(@RequestBody MemberRequestDto requestDto) {
        ResponseCode responseCode = ResponseCode.MEMBER_SAVE;
        return new ResponseEntity(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(memberService.signup(requestDto))
            .build(), HttpStatus.OK);
    }

    /*로그인*/

    /*회원검색*/

    /*회원정보 수정*/

    /*회원탈퇴*/
}
