package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.entity.Likes;
import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.LikesRepository;
import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.service.LikesService;
import com.HowBaChu.howbachu.service.OpinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikesServiceImpl implements LikesService {

    private final OpinService opinService;
    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;


    @Override
    @Transactional
    public Long addLikes(String email, Long opineId) {
        Member member = fetchMember(email);
        Opin opin = opinService.getOpin(opineId, email);

        // 좋아요 중복 검사.
        checkForDuplicateLikes(email, opineId);

        likesRepository.save(Likes.of(member, opin));
        opin.addLikes();
        return opineId;
    }

    @Override
    @Transactional
    public Long cancelLikes(String email, Long opineId) {
        Likes likes = fetchLikes(email, opineId);
        likes.getOpin().cancelLikes();
        likesRepository.delete(likes);
        return opineId;
    }


    public Likes fetchLikes(String email, Long opineId) {
        Likes likes = likesRepository.findLikesByMember_EmailAndOpin_Id(email, opineId).orElseThrow(
            () -> new CustomException(ErrorCode.LIKES_NOT_FOUND)
        );
        return likes;
    }

    public Member fetchMember(String email) {
        return memberRepository.findByEmail(email);
    }

    private void checkForDuplicateLikes(String email, Long opineId) {
        if (likesRepository.existsByMember_EmailAndOpin_Id(email, opineId)) {
            throw new CustomException(ErrorCode.LIKES_ALREADY_EXISTS);
        }
    }
}
