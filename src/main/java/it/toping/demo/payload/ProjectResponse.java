package it.toping.demo.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

public class ProjectResponse {

    private Long id;
    private String projectName;
    private List<FundingResponse> fundings;
    private UserSummary createdBy;
    private Instant creationDateTime;
    private Instant expirationDateTime;
    private Boolean isExpired;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long selectedFunding;
    private Long totalInvestment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<FundingResponse> getFundings() {
        return fundings;
    }

    public void setFundings(List<FundingResponse> fundings) {
        this.fundings = fundings;
    }

    public UserSummary getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserSummary createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public Instant getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(Instant expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public Long getSelectedFunding() {
        return selectedFunding;
    }

    public void setSelectedFunding(Long selectedFunding) {
        this.selectedFunding = selectedFunding;
    }

    public Long getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(Long totalInvestment) {
        this.totalInvestment = totalInvestment;
    }
}
