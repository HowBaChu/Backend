package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.member.StatusResponseDto;
import com.HowBaChu.howbachu.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<?> findMemberDetail(@ApiIgnore Authentication authentication) {
        return ResponseCode.MEMBER_DETAIL.toResponse(
            memberService.findMemberDetail(authentication.getName()));
    }

    /*회원정보 수정*/
    @PatchMapping
    public ResponseEntity<?> updateMemberDetail(@ApiIgnore Authentication authentication,
        @Valid @RequestBody MemberRequestDto.update requestDto) {
        return ResponseCode.MEMBER_UPDATE.toResponse(
            memberService.updateMember(authentication.getName(), requestDto));
    }

    /*회원탈퇴*/
    @DeleteMapping
    public ResponseEntity<?> deleteMember(@ApiIgnore Authentication authentication) {
        memberService.deleteMember(authentication.getName());
        return ResponseCode.MEMBER_DELETE.toResponse(null);
    }

    /*이메일 중복 검사*/
    @GetMapping("/email/{email}/exists")
    public ResponseEntity<?> checkEmailDuplicate(@PathVariable String email) {
        return ResponseCode.MEMBER_EXISTS.toResponse(memberService.checkEmailDuplicate(email));
    }

    /*닉네임 중복 검사*/
    @GetMapping("/username/{username}/exists")
    public ResponseEntity<?> checkUsernameDuplicate(@PathVariable String username) {
        return ResponseCode.MEMBER_EXISTS.toResponse(
            memberService.checkUsernameDuplicate(username));
    }

    @PostMapping("/password-verification")
    public ResponseEntity<?> checkPasswordVerification(
        @Valid @RequestBody MemberRequestDto.passwordVerification requestDto,
        @ApiIgnore Authentication authentication) {
        StatusResponseDto responseDto = memberService.checkPassword(requestDto.getPassword(),
            authentication.getName());
        return responseDto.passwordCheck().toResponse(responseDto);
    }

    /*프로필 사진 추가*/
    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(@ApiIgnore Authentication authentication,
        @RequestPart(value = "file") MultipartFile image) {
        return ResponseCode.AVATAR_UPLOAD.toResponse(
            memberService.uploadAvatar(authentication.getName(), image));
    }

    /*프로필 사진 삭제*/
    @DeleteMapping("/avatar")
    public ResponseEntity<?> deleteAvatar(@ApiIgnore Authentication authentication) {
        memberService.deleteAvatar(authentication.getName());
        return ResponseCode.AVATAR_DELETE.toResponse(null);
    }
}
