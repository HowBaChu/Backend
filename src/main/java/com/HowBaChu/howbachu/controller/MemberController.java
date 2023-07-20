package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.response.ResResult;
import com.HowBaChu.howbachu.service.MemberService;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping(value = "/api/member")
public class MemberController {

    private final MemberService memberService;

    /*회원가입*/
    @PostMapping("/signup")
    public ResponseEntity<ResResult> register(@RequestBody MemberRequestDto requestDto) {
        ResponseCode responseCode = ResponseCode.MEMBER_SAVE;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(memberService.signup(requestDto))
            .build(), HttpStatus.OK);
    }

    /*로그인*/
    @PostMapping("/login")
    public ResponseEntity<ResResult> login(@RequestBody MemberRequestDto requestDto, HttpServletResponse response) {
        ResponseCode responseCode = ResponseCode.MEMBER_LOGIN;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(memberService.login(requestDto,response))
            .build(),HttpStatus.OK);
    }
    /*회원검색*/

    /*회원정보 수정*/

    /*회원탈퇴*/
}
