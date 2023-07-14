package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberResponseDto;
import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.repository.MemberRepository;
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

    @Override
    public MemberResponseDto signup(MemberRequestDto requestDto) {
        Member member = Member.toEntity(requestDto, passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Override
    public void login(MemberRequestDto requestDto) {

    }

    @Override
    public void findMember(String email) {

    }

    @Override
    public void updateMember(String email, MemberRequestDto requestDto) {

    }

    @Override
    public void deleteMember(String email, MemberRequestDto requestDto) {

    }
}
