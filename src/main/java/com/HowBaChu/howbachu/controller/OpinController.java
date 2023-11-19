package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.opin.OpinRequestDto;
import com.HowBaChu.howbachu.service.OpinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/opin")
public class OpinController {

    private final OpinService opinService;

    @PostMapping
    public ResponseEntity<?> postOpin(@RequestBody OpinRequestDto requestDto,
                                      @ApiIgnore Authentication authentication) {
        return ResponseCode.OPIN_SAVE.toResponse(opinService.createOpin(requestDto, authentication.getName(), requestDto.getParentId()));
    }

    @GetMapping
    public ResponseEntity<?> listOpin(@RequestParam(name = "page", defaultValue = "0") int page,
                                      @ApiIgnore Authentication authentication) {
        return ResponseCode.OPIN_LIST.toResponse(opinService.getOpinList(page, authentication.getName()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> listOpin(@PathVariable("id") Long id,
                                      @ApiIgnore Authentication authentication) {
        return ResponseCode.OPIN_LIST.toResponse(opinService.getOpinThread(id, authentication.getName()));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateOpin(@PathVariable("id") String id,
                                        @RequestBody OpinRequestDto requestDto,
                                        @ApiIgnore Authentication authentication) {
        return ResponseCode.OPIN_UPDATE.toResponse(opinService.updateOpin(requestDto, Long.parseLong(id), authentication.getName()));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteOpin(@RequestParam("opinId") Long opinId,
                                        @ApiIgnore Authentication authentication) {
        return ResponseCode.OPIN_DELETE.toResponse(opinService.removeOpin(opinId, authentication.getName()));
    }

    @GetMapping(value = "/my")
    public ResponseEntity<?> listMyOpin(@RequestParam(name = "page", defaultValue = "0") int page,
                                        @ApiIgnore Authentication authentication) {
        return ResponseCode.OPIN_LIST.toResponse(opinService.getMyOpinList(page, authentication.getName()));
    }

}
