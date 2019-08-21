package it.toping.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "tbl_u300_post")
public class Post {

    @Id
    private Long id;

    private String track;

    @Column(name = "img")
    private String imgSource;

    @Column(name = "Tname")
    private String teamName;

    @Column(name = "Tnum")
    private String teamCnt;

    @Column(name = "Cname")
    private String captainName;

    @Column(name = "univ")
    private String organization;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_intro")
    private String itemIntro;



    public Post() { }

    public Post(String track, String imgSource, String teamName, String teamCnt, String captainName, String organization, String itemName, String itemIntro) {
        this.track = track;
        this.imgSource = imgSource;
        this.teamName = teamName;
        this.teamCnt = teamCnt;
        this.captainName = captainName;
        this.organization = organization;
        this.itemName = itemName;
        this.itemIntro = itemIntro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getImgSource() {
        return imgSource;
    }

    public void setImgSource(String imgSource) {
        this.imgSource = imgSource;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamCnt() {
        return teamCnt;
    }

    public void setTeamCnt(String teamCnt) {
        this.teamCnt = teamCnt;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemIntro() {
        return itemIntro;
    }

    public void setItemIntro(String itemIntro) {
        this.itemIntro = itemIntro;
    }
}
