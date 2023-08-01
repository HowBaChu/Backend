package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.jwt.TokenDto;
import com.HowBaChu.howbachu.domain.dto.jwt.TokenResponseDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberResponseDto;
import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.domain.entity.RefreshToken;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.jwt.JwtProvider;
import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.repository.RefreshTokenRepository;
import com.HowBaChu.howbachu.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Override
    public MemberResponseDto signup(MemberRequestDto requestDto) {
        requestDto.encodePassword(passwordEncoder.encode(requestDto.getPassword()));
        return MemberResponseDto.of(memberRepository.save(Member.toEntity(requestDto)));
    }

    @Override
    public TokenResponseDto login(MemberRequestDto requestDto) {

        Member member = memberRepository.findByEmail(requestDto.getEmail());

        if (!validatePassword(requestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_LOGIN_REQUEST);
        }

        TokenDto tokenDto = jwtProvider.generateJwtToken(member.getEmail());

        if (refreshTokenRepository.findByKey(member.getEmail()).isPresent()) {
            refreshTokenRepository.deleteByKey(member.getEmail());
        }
        refreshTokenRepository.save(new RefreshToken(member.getEmail(), tokenDto.getRefreshToken()));

        return new TokenResponseDto(tokenDto.getAccessToken());
    }

    @Override
    public MemberResponseDto findMemberDetail(String email) {
        return MemberResponseDto.of(memberRepository.findByEmail(email));
    }

    @Override
    public MemberResponseDto updateMember(String email, MemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(email);

        if (!member.getEmail().equals(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED_CONTENT);
        }
        requestDto.encodePassword(requestDto.getPassword());
        member.update(requestDto);
        return MemberResponseDto.of(member);
    }

    @Override
    public void deleteMember(String email) {
        memberRepository.deleteById(memberRepository.findByEmail(email).getId());
    }

    @Override
    public boolean checkEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public boolean checkUsernameDuplicate(String username) {
        return memberRepository.existsByUsername(username);
    }

    @Override
    public void logout(String email) {
        refreshTokenRepository.deleteByKey(email);
    }

    public boolean validatePassword(String input, String encoded) {
        return passwordEncoder.matches(input, encoded);
    }


}
