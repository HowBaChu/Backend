package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.core.manager.AWSFileManager;
import com.HowBaChu.howbachu.domain.dto.jwt.TokenDto;
import com.HowBaChu.howbachu.domain.dto.jwt.TokenResponseDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberResponseDto;
import com.HowBaChu.howbachu.domain.dto.member.StatusResponseDto;
import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.domain.entity.RefreshToken;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.jwt.JwtProvider;
import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.repository.RefreshTokenRepository;
import com.HowBaChu.howbachu.repository.VoteRepository;
import com.HowBaChu.howbachu.service.MemberService;
import com.HowBaChu.howbachu.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final VoteRepository voteRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final AWSFileManager awsFileManager;
    private final CookieUtil cookieUtil;

    @Override
    @Transactional
    public MemberResponseDto signup(MemberRequestDto.signup requestDto) {
        requestDto.encodePassword(passwordEncoder.encode(requestDto.getPassword()));
        checkMemberDuplicated("","",requestDto.getEmail(), requestDto.getUsername());
        return MemberResponseDto.of(memberRepository.save(Member.toEntity(requestDto)));
    }

    @Override
    @Transactional
    public TokenResponseDto login(MemberRequestDto.login requestDto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(requestDto.getEmail());

        if (!validatePassword(requestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_LOGIN_REQUEST);
        }

        TokenDto tokenDto = jwtProvider.generateJwtToken(member.getEmail());
        Optional<RefreshToken> refreshTokenOrigin = refreshTokenRepository.findByValue(member.getEmail());
        refreshTokenOrigin.ifPresent(refreshTokenRepository::delete);

        refreshTokenRepository.save(new RefreshToken(tokenDto.getRefreshToken(),member.getEmail(), jwtProvider.getRefreshTokenExpiredTime()));

        // cookie 설정
        cookieUtil.setCookie(response, "Access-Token",tokenDto.getAccessToken(), jwtProvider.getAccessTokenExpiredTime());
        cookieUtil.setCookie(response, "Refresh-Token",tokenDto.getRefreshToken(), jwtProvider.getRefreshTokenExpiredTime());
        cookieUtil.setCookie(response, "member-Id", String.valueOf(member.getId()), cookieUtil.getRemainTime());
        voteRepository.findVoteByEmail(member.getEmail())
            .ifPresent(value -> cookieUtil.setCookie(response, "Vote", value.getSelection().toString(), cookieUtil.getRemainTime()));

        return TokenResponseDto.builder()
            .accessToken(tokenDto.getAccessToken())
            .refreshToken(tokenDto.getRefreshToken())
            .build();
    }

    @Override
    public MemberResponseDto findMemberDetail(String email) {
        return MemberResponseDto.of(memberRepository.findByEmail(email));
    }

    @Override
    @Transactional
    public MemberResponseDto updateMember(String email, MemberRequestDto.update requestDto) {
        Member member = memberRepository.findByEmail(email);
        checkMemberDuplicated(email, member.getUsername(), "", requestDto.getUsername());
        requestDto.encodePassword(passwordEncoder.encode(requestDto.getPassword()));
        member.update(requestDto);
        return MemberResponseDto.of(member);
    }

    @Override
    @Transactional
    public void deleteMember(String email) {
        memberRepository.deleteById(memberRepository.findByEmail(email).getId());
        if (refreshTokenRepository.findById(email).isPresent()) {
            refreshTokenRepository.deleteById(email);
        }
    }

    @Override
    public StatusResponseDto checkEmailDuplicate(String email) {
        return new StatusResponseDto(memberRepository.existsByEmail(email));
    }

    @Override
    public StatusResponseDto checkUsernameDuplicate(String username) {
        return new StatusResponseDto(memberRepository.existsByUsername(username));
    }

    @Override
    @Transactional
    public void logout(String email, HttpServletResponse response){
        cookieUtil.setCookie(response, "Access-Token", "", 0);
        cookieUtil.setCookie(response, "Refresh-Token", "", 0);
        cookieUtil.setCookie(response, "Vote", "", 0);
        cookieUtil.setCookie(response, "member-Id", "", 0);
        refreshTokenRepository.deleteById(email);
    }

    @Override
    @Transactional
    public MemberResponseDto uploadAvatar(String email, MultipartFile image){
        Member member = memberRepository.findByEmail(email);
        member.uploadAvatar(awsFileManager.upload("profile", image));

        return MemberResponseDto.of(member);
    }

    @Override
    @Transactional
    public void deleteAvatar(String email) {
        Member member = memberRepository.findByEmail(email);
        member.uploadAvatar(null);
    }

    @Override
    public StatusResponseDto checkPassword(String password, String email) {
        return new StatusResponseDto(
            validatePassword(password,memberRepository.findByEmail(email).getPassword()));
    }

    public boolean validatePassword(String input, String encoded) {
        return passwordEncoder.matches(input, encoded);
    }

    public void checkMemberDuplicated(String originEmail, String originMemberName, String email, String memberName) {
        if (!originEmail.equals(email) && memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATION);
        }
        if (!originMemberName.equals(memberName) && memberRepository.existsByUsername(memberName)) {
            throw new CustomException(ErrorCode.USERNAME_DUPLICATION);
        }
    }

}
