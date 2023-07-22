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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

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

    /*회원 상세정보*/
    @GetMapping
    public ResponseEntity<ResResult> findMemberDetail(@ApiIgnore Authentication authentication) {
        ResponseCode responseCode = ResponseCode.MEMBER_DETAIL;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(memberService.findMemberDetail(authentication.getName()))
            .build(), HttpStatus.OK);
    }

    /*회원정보 수정*/
    @PatchMapping
    public ResponseEntity<ResResult> updateMemberDetail(@ApiIgnore Authentication authentication, @RequestBody MemberRequestDto requestDto){
        ResponseCode responseCode = ResponseCode.MEMBER_UPDATE;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(memberService.updateMember(authentication.getName(), requestDto))
            .build(), HttpStatus.OK);
    }

    /*회원탈퇴*/

    /*다른 회원 정보*/
}
