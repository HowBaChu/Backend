package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.repository.custom.OpinRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpinRepository extends JpaRepository<Opin, Long>, OpinRepositoryCustom {
}
