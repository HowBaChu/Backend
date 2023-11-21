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
    public Long addLikes(String email, Long opinId) {

        /* 좋아요 누른 사람 */
        Member likedMember = findMemberByEmail(email);
        /* 좋아요 추가될 타겟 */
        Opin opin = findOpinById(opinId);
        /* 좋아요 저장 */
        likesRepository.save(Likes.of(likedMember, opin));
        /* 오핀 Likes 카운트 증가 */
        opin.addLikes();

        return opinId;
    }

    @Override
    @Transactional
    public Long cancelLikes(String cancelerEmail, Long opinId) {

        /* 취소자 이메일 + 최소 대상 Opin 을 통해 `좋아요` 조회 */
        Likes likes = findLikesByEmailAndOpinId(cancelerEmail, opinId);
        /* 좋아요 취소 */
        likes.cancelLikes();
        /* 좋아요 엔티티 삭제 */
        likesRepository.delete(likes);

        return opinId;
    }


    @Override
    public boolean checkDuplicateLike(String email, Long opinId) {
        if (findLikesByEmailAndOpinId(email, opinId) != null) {
            return true;
        }

        return false;
    }

    private Likes findLikesByEmailAndOpinId(String email, Long opinId) {
        return likesRepository.fetchLikesByEmailAndOpinId(email, opinId);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    private Opin findOpinById(Long opinId) {
        return opinRepository.findById(opinId).orElseThrow(
            () -> new CustomException(ErrorCode.OPIN_NOT_FOUND)
        );
    }
}
