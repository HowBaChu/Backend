package com.HowBaChu.howbachu.repository.impl;

import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;
import com.HowBaChu.howbachu.domain.dto.search.SearchResultResponseDto;
import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.Support.Querydsl4RepositorySupport;
import com.HowBaChu.howbachu.repository.custom.OpinRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.HowBaChu.howbachu.domain.entity.QMember.member;
import static com.HowBaChu.howbachu.domain.entity.QOpin.opin;
import static com.HowBaChu.howbachu.domain.entity.QVote.vote;

public class OpinRepositoryImpl extends Querydsl4RepositorySupport implements OpinRepositoryCustom {

    public OpinRepositoryImpl() {
        super(Opin.class);
    }

    private final Pageable defaultSearchOpinPageRequest = PageRequest.of(0, 5);

    @Override
    public Opin fetchOpinByIdAndEmail(Long opinId, String email) {
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
    public List<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchParentOpinSearch(String condition) {
        return select(mapToParentsOpinSearchResponseDto())
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(opin.content.contains(condition), onlyParentOpin())
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
            .where(opin.content.contains(condition), onlyChildOpin())
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
    public Page<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchChildOpinSearch(String condition, Pageable pageable) {
        List<SearchResultResponseDto.ChildOpinSearchResponseDto> content =
            select(mapToChildOpinSearchResponseDto())
                .from(opin)
                .leftJoin(opin.vote, vote)
                .leftJoin(vote.member, member)
                .where(opin.content.contains(condition), onlyChildOpin())
                .offset(defaultSearchOpinPageRequest.getOffset())
                .limit(defaultSearchOpinPageRequest.getPageSize())
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
            .where(opin.parent.id.eq(parentId))
            .orderBy(opin.createdAt.desc())
            .fetch();
    }

    @Override
    public Page<OpinResponseDto> fetchParentOpinList(int page, String email) {
        PageRequest pageRequest = PageRequest.of(page, 20);
        List<OpinResponseDto> content =
            select(mapToOpinResponseDto(email))
                .from(opin)
                .leftJoin(opin.vote, vote)
                .leftJoin(vote.member, member)
                .where(onlyParentOpin())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(opin.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery =
            select(opin.count())
                .from(opin)
                .where(onlyParentOpin());

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchCount);
    }

    @Override
    public Page<OpinResponseDto> fetchMyOpinList(int page, String email) {
        PageRequest pageRequest = PageRequest.of(page, 20);
        List<OpinResponseDto> content =
            select(mapToOpinResponseDto(email))
                .from(opin)
                .leftJoin(opin.vote, vote)
                .leftJoin(vote.member, member)
                .where(member.email.eq(email))
                .orderBy(opin.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = select(opin.count())
            .from(opin)
            .leftJoin(opin.vote, vote)
            .leftJoin(vote.member, member)
            .where(member.email.eq(email));

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchCount);
    }

    private static QBean<OpinResponseDto> mapToOpinResponseDto(String email) {
        return Projections.fields(OpinResponseDto.class,
            opin.id.as("id"),
            member.id.as("memberId"),
            vote.selectSubTitle.as("topicSubTitle"),
            vote.selection.as("selection"),
            member.username.as("nickname"),
            opin.content.as("content"),
            opin.likeCnt.as("likeCnt"),
            isOwner(email).as("isOwner")
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

    private static BooleanExpression isOwner(String email) {
        return member.email.eq(email);
    }

    private static BooleanExpression onlyParentOpin() {
        return opin.parent.isNull();
    }

    private static BooleanExpression onlyChildOpin() {
        return opin.parent.isNotNull();
    }
}
