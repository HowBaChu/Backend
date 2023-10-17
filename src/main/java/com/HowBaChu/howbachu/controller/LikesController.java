package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.likes.LikesRequestDto;
import com.HowBaChu.howbachu.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/likes")
public class LikesController {

    private final LikesService likesService;

    @PostMapping
    public ResponseEntity<?> addLikes(@RequestBody LikesRequestDto requestDto, @ApiIgnore Authentication authentication) {
        return ResponseCode.LIKES_ADD.toResponse(likesService.addLikes(authentication.getName(), requestDto.getOpinId()));
    }

    @DeleteMapping
    public ResponseEntity<?> cancelLikes(@RequestBody LikesRequestDto requestDto, @ApiIgnore Authentication authentication) {
        return ResponseCode.LIKES_CANCEL.toResponse(likesService.cancelLikes(authentication.getName(), requestDto.getOpinId()));
    }
}
