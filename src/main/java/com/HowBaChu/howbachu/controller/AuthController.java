package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.response.ResResult;
import com.HowBaChu.howbachu.service.MailService;
import com.HowBaChu.howbachu.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    private final MemberService memberService;
    private final MailService mailService;

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
    public ResponseEntity<ResResult> login(@RequestBody MemberRequestDto requestDto) {
        ResponseCode responseCode = ResponseCode.MEMBER_LOGIN;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(memberService.login(requestDto))
            .build(), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResResult> logout(@ApiIgnore Authentication authentication) {
        memberService.logout(authentication.getName());
        ResponseCode responseCode = ResponseCode.MEMBER_LOGOUT;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .build(), HttpStatus.OK);
    }

    @PostMapping("/mail-verification")
    public ResponseEntity<ResResult> mailSend(@RequestParam String email){
        mailService.sendMessage(email);
        ResponseCode responseCode = ResponseCode.VERIFICATION_SEND;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .build(), HttpStatus.OK);
    }

}
