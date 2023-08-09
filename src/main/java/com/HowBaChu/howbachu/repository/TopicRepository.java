package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.repository.custom.TopicRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long>, TopicRepositoryCustom {
}
