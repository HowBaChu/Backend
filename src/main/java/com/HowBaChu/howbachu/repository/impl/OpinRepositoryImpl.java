package com.HowBaChu.howbachu.repository.impl;

import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;
import com.HowBaChu.howbachu.domain.dto.opin.TrendingOpinResponseDto;
import com.HowBaChu.howbachu.domain.dto.search.SearchResultResponseDto;
import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.domain.entity.QLikes;
import com.HowBaChu.howbachu.domain.entity.QOpin;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.Support.Querydsl4RepositorySupport;
import com.HowBaChu.howbachu.repository.custom.OpinRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.HowBaChu.howbachu.domain.entity.QLikes.likes;
import static com.HowBaChu.howbachu.domain.entity.QMember.member;
import static com.HowBaChu.howbachu.domain.entity.QOpin.opin;
import static com.HowBaChu.howbachu.domain.entity.QVote.vote;

public class OpinRepositoryImpl extends Querydsl4RepositorySupport implements OpinRepositoryCustom {

    public OpinRepositoryImpl() {
        super(Opin.class);
    }

    @Override
    public Opin fetchOpin(Long opinId, String email) {
        return Optional.ofNullable(
            select(opin)
                .from(opin)
                .leftJoin(opin.vote, vote).fetchJoin()
                .leftJoin(vote.member, member).fetchJoin()
                .where(opin.id.eq(opinId).and(member.email.eq(email)))
                .fetchOne()
        ).orElseThrow(() -> new CustomException(ErrorCode.OPIN_NOT_FOUND));
    }

    @Override
    public Page<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchParentOpinSearch(String condition, String email, Pageable pageable) {
        final var content =
            select(mapToParentsOpinSearchResponseDtoAddParent(email))
                .from(opin)
                .leftJoin(opin.vote, vote)
                .leftJoin(vote.member, member)
                .where(opin.content.contains(condition), onlyParentOpin())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(opin.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = select(opin.count())
            .from(opin)
            .where(opin.content.contains(condition), onlyParentOpin());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchChildOpinSearch(String condition, String email, Pageable pageable) {
        final var content =
            select(mapToChildOpinSearchResponseDto(email))
                .from(opin)
                .leftJoin(opin.vote, vote)
                .leftJoin(vote.member, member)
                .where(opin.content.contains(condition), onlyChildOpin())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(opin.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery =
            select(opin.count())
                .from(opin)
                .where(opin.content.contains(condition), onlyChildOpin());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public OpinResponseDto fetchParentOpin(Long opinId, String email) {
        return select(mapToOpinResponseDto(email))
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(onlyParentOpin(), opin.id.eq(opinId))
            .fetchOne();
    }

    @Override
    public List<OpinResponseDto> fetchChildOpinList(Long parentId, String email) {
        return select(mapToOpinResponseDto(email))
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(onlyChildOpin(), opin.parent.id.eq(parentId))
            .orderBy(opin.createdAt.desc())
            .fetch();
    }

    @Override
    public Page<OpinResponseDto> fetchParentOpinList(Pageable pageable, String email) {
        List<OpinResponseDto> content =
            select(mapToOpinResponseDto(email))
                .from(opin)
                .leftJoin(opin.vote, vote)
                .leftJoin(vote.member, member)
                .where(onlyParentOpin())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(opin.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery =
            select(opin.count())
                .from(opin)
                .where(onlyParentOpin());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private static BooleanExpression isLiked(String email) {
        return JPAExpressions
            .selectFrom(likes)
            .where(likes.member.email.eq(email).and(likes.opin.id.eq(opin.id)))
            .exists();
    }

    @Override
    public Page<OpinResponseDto> fetchMyOpinList(Pageable pageable, String email) {
        List<OpinResponseDto> content =
            select(mapToOpinResponseDto(email))
                .from(opin)
                .leftJoin(opin.vote, vote)
                .leftJoin(vote.member, member)
                .where(member.email.eq(email))
                .orderBy(opin.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = select(opin.count())
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(member.email.eq(email));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public TrendingOpinResponseDto fetchHotOpin() {
        return Optional.ofNullable(select(Projections.fields(TrendingOpinResponseDto.class,
            opin.id.as("id"),
            member.id.as("memberId"),
            vote.selectSubTitle.as("topicSubTitle"),
            vote.selection.as("selection"),
            member.username.as("nickname"),
            member.avatar.as("profileImg"),
            opin.content.as("content"),
            opin.likeCnt.as("likeCnt"))
        ).from(likes)
            .leftJoin(likes.opin, opin)
            .leftJoin(likes.member, member)
            .leftJoin(opin.vote, vote)
            .groupBy(opin.id)
            .orderBy(opin.id.count().desc(), opin.createdAt.desc())
            .fetchFirst()).orElseThrow(() -> new CustomException(ErrorCode.OPIN_HOT_NOT_FOUND));
    }

    private static QBean<OpinResponseDto> mapToOpinResponseDto(String email) {
        return Projections.fields(OpinResponseDto.class,
            opin.id.as("id"),
            member.id.as("memberId"),
            vote.selectSubTitle.as("topicSubTitle"),
            vote.selection.as("selection"),
            member.username.as("nickname"),
            member.avatar.as("profileImg"),
            opin.content.as("content"),
            opin.likeCnt.as("likeCnt"),
            member.email.eq(email).as("isOwner"),
            isLiked(email).as("isLiked")
        );
    }

    private static QBean<SearchResultResponseDto.ChildOpinSearchResponseDto> mapToChildOpinSearchResponseDto(String email) {
        return Projections.fields(SearchResultResponseDto.ChildOpinSearchResponseDto.class,
            opin.id.as("opinId"),
            member.avatar.as("profileImg"),
            member.username.as("nickname"),
            opin.content.as("content"),
            opin.likeCnt.as("likeCnt"),
            vote.selectSubTitle.as("topicSubtitle"),
            opin.parent.id.as("parentOpinId"),
            isLiked(email).as("isLiked")
        );
    }

    private static QBean<SearchResultResponseDto.ParentsOpinSearchResponseDto> mapToParentsOpinSearchResponseDtoAddParent(String email) {
        return Projections.fields(SearchResultResponseDto.ParentsOpinSearchResponseDto.class,
            opin.id.as("opinId"),
            member.avatar.as("profileImg"),
            member.username.as("nickname"),
            opin.content.as("content"),
            opin.likeCnt.as("likeCnt"),
            vote.selectSubTitle.as("topicSubtitle"),
            opin.parent.id.as("parentOpinId"),
            isLiked(email).as("isLiked")
        );
    }

    private static BooleanExpression onlyParentOpin() {
        return opin.parent.isNull();
    }

    private static BooleanExpression onlyChildOpin() {
        return opin.parent.isNotNull();
    }
}
