package com.HowBaChu.howbachu.repository.impl;

import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;
import com.HowBaChu.howbachu.domain.dto.search.SearchResultResponseDto;
import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.repository.Support.Querydsl4RepositorySupport;
import com.HowBaChu.howbachu.repository.custom.OpinRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
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

    private final Pageable defaultPage = PageRequest.of(0, 5);

    @Override
    public List<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchParentOpinSearch(String condition) {
        return select(mapToParentsOpinSearchResponseDto())
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(opin.content.contains(condition), opin.parent.isNull())
            .offset(defaultPage.getOffset())
            .limit(defaultPage.getPageSize())
            .orderBy(opin.modifiedAt.desc())
            .fetch();
    }

    @Override
    public List<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchChildOpinSearch(String condition) {
        return select(mapToChildOpinSearchResponseDto())
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(opin.content.contains(condition), opin.parent.isNotNull())
            .offset(defaultPage.getOffset())
            .limit(defaultPage.getPageSize())
            .orderBy(opin.modifiedAt.desc())
            .fetch();
    }

    @Override
    public Page<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchParentOpinSearch(String condition, Pageable pageable) {

        List<SearchResultResponseDto.ParentsOpinSearchResponseDto> content =
            select(mapToParentsOpinSearchResponseDtoAddParent())
                .from(opin)
                .leftJoin(opin.vote, vote)
                .leftJoin(vote.member, member)
                .where(opin.content.contains(condition), opin.parent.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(opin.modifiedAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = select(opin.count())
            .from(opin)
            .where(opin.content.contains(condition), opin.parent.isNull());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchChildOpinSearch(String condition, Pageable pageable) {

        List<SearchResultResponseDto.ChildOpinSearchResponseDto> content =
            select(mapToChildOpinSearchResponseDto())
                .from(opin)
                .leftJoin(opin.vote, vote)
                .leftJoin(vote.member, member)
                .where(opin.content.contains(condition), opin.parent.isNotNull())
                .offset(defaultPage.getOffset())
                .limit(defaultPage.getPageSize())
                .orderBy(opin.modifiedAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = select(opin.count())
            .from(opin)
            .where(opin.content.contains(condition), opin.parent.isNotNull());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public List<OpinResponseDto> fetchOpinChildList(Long parentId) {
        return select(mapToOpinResponseDto())
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(opin.parent.id.eq(parentId))
            .fetch();
    }

    @Override
    public OpinResponseDto fetchParentOpin(Long opinId) {
        return select(mapToOpinResponseDto())
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(opin.id.eq(opinId))
            .fetchOne();
    }

    private static QBean<OpinResponseDto> mapToOpinResponseDto() {
        return Projections.fields(OpinResponseDto.class,
            opin.id.as("id"),
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
            opin.parent.as("parentOpinId")
        );
    }

    private static QBean<SearchResultResponseDto.ParentsOpinSearchResponseDto> mapToParentsOpinSearchResponseDtoAddParent() {
        return Projections.fields(SearchResultResponseDto.ParentsOpinSearchResponseDto.class,
            member.avatar.as("profileImg"),
            member.username.as("author"),
            opin.content.as("content"),
            opin.likeCnt.as("likeCnt"),
            vote.selectSubTitle.as("topicSubtitle"),
            opin.parent.as("parentOpinId")
        );
    }
}
