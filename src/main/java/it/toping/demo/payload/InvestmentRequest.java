package it.toping.demo.payload;

import javax.validation.constraints.NotNull;

public class InvestmentRequest {

    @NotNull
    private Long fundingId;

    public Long getFundingId() {
        return fundingId;
    }

    public void setFundingId(Long fundingId) {
        this.fundingId = fundingId;
    }
}
