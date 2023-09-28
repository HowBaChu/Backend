package com.HowBaChu.howbachu.domain.entity.embedded;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ReportPK implements Serializable {
    @Column(name = "reporter_id")
    private Long reporterId;
    @Column(name = "reported_id")
    private Long reportedId;
}
