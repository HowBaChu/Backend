package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/likes")
public class LikesController {

    private final LikesService likesService;

    private String loggedInEmail() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(Authentication::getName)
            .orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
            );
    }

    @PostMapping
    public ResponseEntity<?> toggleLikes(@RequestParam("opinId") Long opinId) {
        /* 로그인한 회원의 이메일 */
        String email = loggedInEmail();
        return likesService.checkDuplicateLike(email, opinId)
            ? ResponseCode.LIKES_CANCEL.toResponse(likesService.cancelLikes(email, opinId))
            : ResponseCode.LIKES_ADD.toResponse(likesService.addLikes(email, opinId));
    }

}
