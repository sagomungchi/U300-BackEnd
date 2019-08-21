package it.toping.demo.payload;

import java.time.Instant;

public class UserProfile {

    private Long id;
    private String userName;
    private String teamName;
    private Instant joinedAt;
    private Long projectCnt;
    private Long investCnt;

    public UserProfile(Long id, String userName, String teamName, Instant joinedAt, Long projectCnt, Long investCnt) {
        this.id = id;
        this.userName = userName;
        this.teamName = teamName;
        this.joinedAt = joinedAt;
        this.projectCnt = projectCnt;
        this.investCnt = investCnt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Long getProjectCnt() {
        return projectCnt;
    }

    public void setProjectCnt(Long projectCnt) {
        this.projectCnt = projectCnt;
    }

    public Long getInvestCnt() {
        return investCnt;
    }

    public void setInvestCnt(Long investCnt) {
        this.investCnt = investCnt;
    }
}
