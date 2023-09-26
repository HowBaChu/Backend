package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.domain.entity.Report;
import com.HowBaChu.howbachu.domain.entity.embedded.ReportPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, ReportPK> {

    List<Report> findByReported(Member reported);
}
