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

    private final String ACCESS_TOKEN = "Access-Token";
    private final String REFRESH_TOKEN = "Refresh-Token";
    private final String MEMBER_ID = "Member-Id";
    private final String VOTE = "Vote";
    private final String PROFILE_URL = "profile";

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
        checkMemberDuplicateByEmail(requestDto.getEmail());
        checkMemberDuplicateByMemberName("", requestDto.getUsername());
        return MemberResponseDto.of(memberRepository.save(Member.toEntity(requestDto)));
    }

    @Override
    @Transactional
    public TokenResponseDto login(MemberRequestDto.login requestDto, HttpServletResponse response) {
        Member member = findMemberByEmail(requestDto.getEmail());

        if (!validatePassword(requestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_LOGIN_REQUEST);
        }

        TokenDto tokenDto = jwtProvider.generateJwtToken(member.getEmail());
        Optional<RefreshToken> refreshTokenOrigin = refreshTokenRepository.findByValue(member.getEmail());
        refreshTokenOrigin.ifPresent(refreshTokenRepository::delete);

        refreshTokenRepository.save(new RefreshToken(tokenDto.getRefreshToken(), member.getEmail(), jwtProvider.getRefreshTokenExpiredTime()));

        // cookie 설정
        // 리프레쉬 토큰 외에는 세션 토큰.
        cookieUtil.setCookie(response, REFRESH_TOKEN, tokenDto.getRefreshToken(), jwtProvider.getRefreshTokenExpiredTime());
        cookieUtil.setCookie(response, ACCESS_TOKEN, tokenDto.getAccessToken());

        return TokenResponseDto.builder()
            .accessToken(tokenDto.getAccessToken())
            .refreshToken(tokenDto.getRefreshToken())
            .build();
    }

    @Override
    public MemberResponseDto findMemberDetail(String email) {
        return MemberResponseDto.of(findMemberByEmail(email));
    }

    @Override
    @Transactional
    public MemberResponseDto updateMember(String email, MemberRequestDto.update requestDto) {
        Member member = findMemberByEmail(email);
        if (requestDto.getUsername() != null) {
            checkMemberDuplicateByMemberName(member.getUsername(), requestDto.getUsername());
        }
        if (requestDto.getPassword() != null) {
            requestDto.encodePassword(passwordEncoder.encode(requestDto.getPassword()));
        }
        member.update(requestDto);
        return MemberResponseDto.of(member);
    }

    @Override
    @Transactional
    public void deleteMember(String email) {
        memberRepository.deleteById(findMemberByEmail(email).getId());
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
    public void logout(String email, HttpServletResponse response) {
        cookieUtil.deleteCookie(response, ACCESS_TOKEN, REFRESH_TOKEN, MEMBER_ID, VOTE);
        refreshTokenRepository.deleteById(email);
    }

    @Override
    @Transactional
    public MemberResponseDto uploadAvatar(String email, MultipartFile image) {
        Member member = findMemberByEmail(email);
        member.uploadAvatar(awsFileManager.upload(PROFILE_URL, image));

        return MemberResponseDto.of(member);
    }

    @Override
    @Transactional
    public void deleteAvatar(String email) {
        Member member = findMemberByEmail(email);
        member.uploadAvatar(null);
    }

    @Override
    public StatusResponseDto checkPassword(String password, String email) {
        return new StatusResponseDto(
            validatePassword(password, findMemberByEmail(email).getPassword()));
    }

    private boolean validatePassword(String input, String encoded) {
        return passwordEncoder.matches(input, encoded);
    }

    private void checkMemberDuplicateByEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATION);
        }
    }

    private void checkMemberDuplicateByMemberName(String originMemberName, String memberName) {
        if (!originMemberName.equals(memberName) && memberRepository.existsByUsername(memberName)) {
            throw new CustomException(ErrorCode.USERNAME_DUPLICATION);
        }
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

}
