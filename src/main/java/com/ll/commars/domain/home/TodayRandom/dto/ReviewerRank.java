package com.ll.commars.domain.home.TodayRandom.dto;

public class ReviewerRank {

    private String reviewer;
    private int count;

    public ReviewerRank(String reviewer, int count) {
        this.reviewer = reviewer;
        this.count = count;
    }

    // Getter, Setter
    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
