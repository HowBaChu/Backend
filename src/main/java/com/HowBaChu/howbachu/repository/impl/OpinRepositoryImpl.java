package com.HowBaChu.howbachu.repository.impl;

import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;
import com.HowBaChu.howbachu.domain.dto.search.SearchResultResponseDto;
import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.repository.Support.Querydsl4RepositorySupport;
import com.HowBaChu.howbachu.repository.custom.OpinRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.HowBaChu.howbachu.domain.entity.QMember.member;
import static com.HowBaChu.howbachu.domain.entity.QOpin.opin;
import static com.HowBaChu.howbachu.domain.entity.QVote.vote;

public class OpinRepositoryImpl extends Querydsl4RepositorySupport implements OpinRepositoryCustom {

    public OpinRepositoryImpl() {
        super(Opin.class);
    }

    private final Pageable defaultSearchOpinPageRequest = PageRequest.of(0, 5);

    @Override
    public List<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchParentOpinSearch(String condition) {
        return select(mapToParentsOpinSearchResponseDto())
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(opin.content.contains(condition), parentOpin())
            .offset(defaultSearchOpinPageRequest.getOffset())
            .limit(defaultSearchOpinPageRequest.getPageSize())
            .orderBy(opin.createdAt.desc())
            .fetch();
    }

    @Override
    public List<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchChildOpinSearch(String condition) {
        return select(mapToChildOpinSearchResponseDto())
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(opin.content.contains(condition), childOpin())
            .offset(defaultSearchOpinPageRequest.getOffset())
            .limit(defaultSearchOpinPageRequest.getPageSize())
            .orderBy(opin.createdAt.desc())
            .fetch();
    }

    @Override
    public Page<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchParentOpinSearch(String condition, Pageable pageable) {
        List<SearchResultResponseDto.ParentsOpinSearchResponseDto> content =
            select(mapToParentsOpinSearchResponseDtoAddParent())
                .from(opin)
                .leftJoin(opin.vote, vote)
                .leftJoin(vote.member, member)
                .where(opin.content.contains(condition), parentOpin())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(opin.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = select(opin.count())
            .from(opin)
            .where(opin.content.contains(condition), parentOpin());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchChildOpinSearch(String condition, Pageable pageable) {
        List<SearchResultResponseDto.ChildOpinSearchResponseDto> content =
            select(mapToChildOpinSearchResponseDto())
                .from(opin)
                .leftJoin(opin.vote, vote)
                .leftJoin(vote.member, member)
                .where(opin.content.contains(condition), childOpin())
                .offset(defaultSearchOpinPageRequest.getOffset())
                .limit(defaultSearchOpinPageRequest.getPageSize())
                .orderBy(opin.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery =
            select(opin.count())
                .from(opin)
                .where(opin.content.contains(condition), childOpin());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public OpinResponseDto fetchParentOpin(Long opinId) {
        return select(mapToOpinResponseDto())
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(parentOpin(), opin.id.eq(opinId))
            .fetchOne();
    }

    @Override
    public List<OpinResponseDto> fetchOpinChildList(Long parentId) {
        return select(mapToOpinResponseDto())
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(opin.parent.id.eq(parentId))
            .orderBy(opin.createdAt.desc())
            .fetch();
    }

    @Override
    public Page<OpinResponseDto> fetchParentOpinList(int page) {
        PageRequest pageRequest = PageRequest.of(page, 20);
        List<OpinResponseDto> content =
            select(mapToOpinResponseDto())
                .from(opin)
                .leftJoin(opin.vote, vote)
                .leftJoin(vote.member, member)
                .where(parentOpin())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(opin.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery =
            select(opin.count())
                .from(opin)
                .where(parentOpin());

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchCount);
    }

    private static QBean<OpinResponseDto> mapToOpinResponseDto() {
        return Projections.fields(OpinResponseDto.class,
            opin.id.as("id"),
            member.id.as("memberId"),
            vote.selectSubTitle.as("topicSubTitle"),
            vote.selection.as("selection"),
            member.username.as("nickname"),
            opin.content.as("content"),
            opin.likeCnt.as("likeCnt")
        );
    }

    private static QBean<SearchResultResponseDto.ParentsOpinSearchResponseDto> mapToParentsOpinSearchResponseDto() {
        return Projections.fields(SearchResultResponseDto.ParentsOpinSearchResponseDto.class,
            member.avatar.as("profileImg"),
            member.username.as("author"),
            opin.content.as("content"),
            opin.likeCnt.as("likeCnt"),
            vote.selectSubTitle.as("topicSubtitle")
        );
    }

    private static QBean<SearchResultResponseDto.ChildOpinSearchResponseDto> mapToChildOpinSearchResponseDto() {
        return Projections.fields(SearchResultResponseDto.ChildOpinSearchResponseDto.class,
            member.avatar.as("profileImg"),
            member.username.as("author"),
            opin.content.as("content"),
            opin.likeCnt.as("likeCnt"),
            vote.selectSubTitle.as("topicSubtitle"),
            opin.parent.id.as("parentOpinId")
        );
    }

    private static QBean<SearchResultResponseDto.ParentsOpinSearchResponseDto> mapToParentsOpinSearchResponseDtoAddParent() {
        return Projections.fields(SearchResultResponseDto.ParentsOpinSearchResponseDto.class,
            member.avatar.as("profileImg"),
            member.username.as("author"),
            opin.content.as("content"),
            opin.likeCnt.as("likeCnt"),
            vote.selectSubTitle.as("topicSubtitle"),
            opin.parent.id.as("parentOpinId")
        );
    }

    private static BooleanExpression parentOpin() {
        return opin.parent.isNull();
    }

    private static BooleanExpression childOpin() {
        return opin.parent.isNotNull();
    }
}
