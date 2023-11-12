package com.HowBaChu.howbachu.domain.constants;

public enum ReportCriteria {

    OPIN_SUSPENSION_COUNT(5),
    MEMBER_SUSPENSION_COUNT(20);

    private final int count;

    ReportCriteria(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
