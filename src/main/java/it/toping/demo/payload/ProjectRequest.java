package it.toping.demo.payload;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class ProjectRequest {

    @NotBlank
    @Size(max = 140)
    private String projectName;

    @NotNull
    @Size(min = 2, max = 6)
    @Valid
    private List<FundingRequest> fundings;

    @NotNull
    @Valid
    private FundingLength fundingLength;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<FundingRequest> getFundings() {
        return fundings;
    }

    public void setFundings(List<FundingRequest> fundings) {
        this.fundings = fundings;
    }

    public FundingLength getFundingLength() {
        return fundingLength;
    }

    public void setFundingLength(FundingLength fundingLength) {
        this.fundingLength = fundingLength;
    }
}
