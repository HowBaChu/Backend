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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final AWSFileManager awsFileManager;

    @Override
    public MemberResponseDto signup(MemberRequestDto.signup requestDto) {
        requestDto.encodePassword(passwordEncoder.encode(requestDto.getPassword()));
        return MemberResponseDto.of(memberRepository.save(Member.toEntity(requestDto)));
    }

    @Override
    public TokenResponseDto login(MemberRequestDto.login requestDto) {

        Member member = memberRepository.findByEmail(requestDto.getEmail());

        if (!validatePassword(requestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_LOGIN_REQUEST);
        }

        TokenDto tokenDto = jwtProvider.generateJwtToken(member.getEmail());

        if (refreshTokenRepository.findByKey(member.getEmail()).isPresent()) {
            refreshTokenRepository.deleteByKey(member.getEmail());
        }
        refreshTokenRepository.save(new RefreshToken(member.getEmail(), tokenDto.getRefreshToken()));

        return new TokenResponseDto(tokenDto.getAccessToken(),tokenDto.getRefreshToken());
    }

    @Override
    public MemberResponseDto findMemberDetail(String email) {
        return MemberResponseDto.of(memberRepository.findByEmail(email));
    }

    @Override
    public MemberResponseDto updateMember(String email, MemberRequestDto.update requestDto) {
        Member member = memberRepository.findByEmail(email);
        requestDto.encodePassword(passwordEncoder.encode(requestDto.getPassword()));
        member.update(requestDto);
        return MemberResponseDto.of(member);
    }

    @Override
    public void deleteMember(String email) {
        memberRepository.deleteById(memberRepository.findByEmail(email).getId());
        if (refreshTokenRepository.findByKey(email).isPresent()) {
            refreshTokenRepository.deleteByKey(email);
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
    public void logout(String email) {
        refreshTokenRepository.deleteByKey(email);
    }

    @Override
    public MemberResponseDto uploadAvatar(String email, MultipartFile image){
        Member member = memberRepository.findByEmail(email);
        member.uploadAvatar(awsFileManager.upload("profile", image));

        return MemberResponseDto.of(member);
    }

    @Override
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


}
