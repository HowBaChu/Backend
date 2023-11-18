package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.entity.Likes;
import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.LikesRepository;
import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.repository.OpinRepository;
import com.HowBaChu.howbachu.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikesServiceImpl implements LikesService {

    private final OpinRepository opinRepository;
    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;


    @Override
    @Transactional
    public Long addLikes(String email, Long opineId) {

        /* 좋아요 누른 사람 */
        Member likedMember = findMemberByEmail(email);
        /* 좋아요 추가될 타겟 */
        Opin opin = findOpinById(opineId);

        /* 좋아요 중복 검사*/
        checkDuplicateLike(email, opineId);

        /* 좋아요 저장 */
        likesRepository.save(Likes.of(likedMember, opin));
        /* 오핀 Likes 카운트 증가 */
        opin.addLikes();

        return opineId;
    }

    @Override
    @Transactional
    public Long cancelLikes(String cancelerEmail, Long opineId) {

        /* 취소자 이메일 + 최소 대상 Opin 을 통해 `좋아요` 조회 */
        Likes likes = findLikesByEmailAndOpinId(cancelerEmail, opineId);

        /* 좋아요 취소 */
        likes.cancelLikes();
        /* 좋아요 엔티티 삭제 */
        likesRepository.delete(likes);
        return opineId;
    }


    private Likes findLikesByEmailAndOpinId(String email, Long opineId) {
        return likesRepository.fetchLikesByEmailAndOpinId(email, opineId);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    private Opin findOpinById(Long opineId) {
        return opinRepository.findById(opineId).orElseThrow(
            () -> new CustomException(ErrorCode.OPIN_NOT_FOUND)
        );
    }

    private void checkDuplicateLike(String email, Long opineId) {
        if (findLikesByEmailAndOpinId(email, opineId) == null) {
            throw new CustomException(ErrorCode.LIKES_ALREADY_EXISTS);
        }
    }
}
