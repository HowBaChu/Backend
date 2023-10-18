package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.opin.OpinRequestDto;
import com.HowBaChu.howbachu.service.OpinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/opin")
public class OpinController {

    private final OpinService opinService;

    @PostMapping
    public ResponseEntity<?> postOpin(@RequestBody OpinRequestDto requestDto, @ApiIgnore Authentication authentication) {
        return ResponseCode.OPIN_SAVE.toResponse(opinService.createOpin(requestDto, authentication.getName(), requestDto.getParentId()));
    }

    @GetMapping
    public ResponseEntity<?> listOpin(@RequestParam(name = "page", defaultValue = "0") int page) {
        return ResponseCode.OPIN_LIST.toResponse(opinService.getOpinList(page));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> listOpin(@PathVariable("id") String id) {
        return ResponseCode.OPIN_LIST.toResponse(opinService.getOpinThread(Long.parseLong(id)));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateOpin(@PathVariable("id") String id, @RequestBody OpinRequestDto requestDto, @ApiIgnore Authentication authentication) {
        return ResponseCode.OPIN_UPDATE.toResponse(opinService.updateOpin(requestDto, Long.parseLong(id), authentication.getName()));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteOpin(@PathVariable("id") String id, @ApiIgnore Authentication authentication) {
        return ResponseCode.OPIN_DELETE.toResponse(opinService.removeOpin(Long.parseLong(id), authentication.getName()));
    }

    @GetMapping(value = "/member")
    public ResponseEntity<?> listMyOpin(@RequestParam(name = "id") Long memberId, @RequestParam(name = "page", defaultValue = "0") int page) {
        return ResponseCode.OPIN_LIST.toResponse(opinService.getOpinListForMember(memberId, page));
    }

}
