package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.response.ResResult;
import com.HowBaChu.howbachu.service.MemberService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/member")
public class MemberController {

    private final MemberService memberService;

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
    public ResponseEntity<ResResult> updateMemberDetail(@ApiIgnore Authentication authentication,
        @RequestBody MemberRequestDto requestDto) {
        ResponseCode responseCode = ResponseCode.MEMBER_UPDATE;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(memberService.updateMember(authentication.getName(), requestDto))
            .build(), HttpStatus.OK);
    }

    /*회원탈퇴*/
    @DeleteMapping
    public ResponseEntity<ResResult> deleteMember(@ApiIgnore Authentication authentication) {
        ResponseCode responseCode = ResponseCode.MEMBER_DELETE;
        memberService.deleteMember(authentication.getName());
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .build(), HttpStatus.OK);
    }

    /*이메일 중복 검사*/
    @GetMapping("/email/{email}/exists")
    public ResponseEntity<ResResult> checkEmailDuplicate(@PathVariable String email) {
        ResponseCode responseCode = ResponseCode.MEMBER_EXISTS;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(memberService.checkEmailDuplicate(email))
            .build(), HttpStatus.OK);
    }

    /*닉네임 중복 검사*/
    @GetMapping("/username/{username}/exists")
    public ResponseEntity<ResResult> checkUsernameDuplicate(@PathVariable String username) {
        ResponseCode responseCode = ResponseCode.MEMBER_EXISTS;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(memberService.checkUsernameDuplicate(username))
            .build(), HttpStatus.OK);
    }

    /*프로필 사진 추가*/
    @PostMapping("/avatar")
    public ResponseEntity<ResResult> uploadAvatar(
        @ApiIgnore Authentication authentication,
        @RequestPart(value = "file")
        MultipartFile image) throws IOException {
        ResponseCode responseCode = ResponseCode.AVATAR_UPLOAD;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(memberService.uploadImage(authentication.getName(), image))
            .build(),HttpStatus.OK);
    }
}
