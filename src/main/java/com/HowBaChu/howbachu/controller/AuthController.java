package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.service.MemberService;
import com.HowBaChu.howbachu.service.impl.MailServiceImpl;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final MailServiceImpl mailService;

    /*회원가입*/
    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody MemberRequestDto.signup requestDto) {
        return ResponseCode.MEMBER_SAVE.toResponse(memberService.signup(requestDto));
    }

    /*로그인*/
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody MemberRequestDto.login requestDto,
        HttpServletResponse response) {
        return ResponseCode.MEMBER_LOGIN.toResponse(memberService.login(requestDto, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@ApiIgnore Authentication authentication,
        HttpServletResponse response) {
        memberService.logout(authentication.getName(), response);
        return ResponseCode.MEMBER_LOGOUT.toResponse(null);
    }

    @PostMapping("/mail-verification")
    public ResponseEntity<?> mailSend(@RequestParam String email) {
        mailService.sendMessage(email);
        return ResponseCode.VERIFICATION_SEND.toResponse(null);
    }

    @GetMapping("/mail-verification")
    public ResponseEntity<?> certificate(@RequestParam String email,
        @RequestParam String inputCode) {
        return mailService.certificate(email, inputCode).toResponse(null);
    }
}
