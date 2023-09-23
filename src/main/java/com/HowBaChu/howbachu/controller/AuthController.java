package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.response.ResResult;
import com.HowBaChu.howbachu.service.MemberService;
import com.HowBaChu.howbachu.service.impl.MailServiceImpl;
import java.net.http.HttpResponse;
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
    public ResponseEntity<ResResult> register(@Valid @RequestBody MemberRequestDto.signup requestDto) {
        ResponseCode responseCode = ResponseCode.MEMBER_SAVE;
        return responseCode.toResponse(memberService.signup(requestDto));
    }

    /*로그인*/
    @PostMapping("/login")
    public ResponseEntity<ResResult> login(@Valid @RequestBody MemberRequestDto.login requestDto, HttpServletResponse response) {
        ResponseCode responseCode = ResponseCode.MEMBER_LOGIN;
        return responseCode.toResponse(memberService.login(requestDto, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResResult> logout(@ApiIgnore Authentication authentication) {
        memberService.logout(authentication.getName());
        ResponseCode responseCode = ResponseCode.MEMBER_LOGOUT;
        return responseCode.toResponse(null);
    }

    @PostMapping("/mail-verification")
    public ResponseEntity<ResResult> mailSend(@RequestParam String email){
        mailService.sendMessage(email);
        ResponseCode responseCode = ResponseCode.VERIFICATION_SEND;
        return responseCode.toResponse(null);
    }

    @GetMapping("/mail-verification")
    public ResponseEntity<ResResult> certificate(@RequestParam String email,
        @RequestParam String inputCode) {
        ResponseCode responseCode = mailService.certificate(email, inputCode)
            ? ResponseCode.VERIFICATION_SUCCESS
            : ResponseCode.VERIFICATION_FAILED;
        return responseCode.toResponse(null);
    }
}
