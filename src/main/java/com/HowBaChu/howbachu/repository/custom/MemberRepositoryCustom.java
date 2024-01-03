package com.HowBaChu.howbachu.repository.custom;

import com.HowBaChu.howbachu.domain.entity.Member;
import org.springframework.cache.annotation.Cacheable;

public interface MemberRepositoryCustom {

    Member findByEmail(String email);

}
