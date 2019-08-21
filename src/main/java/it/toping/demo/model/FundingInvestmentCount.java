package it.toping.demo.model;

public class FundingInvestmentCount {

    private Long fundingId;
    private Long investmentTotal;

    public FundingInvestmentCount(Long fundingId, Long investmentTotal) {
        this.fundingId = fundingId;
        this.investmentTotal = investmentTotal;
    }

    public Long getFundingId() {
        return fundingId;
    }

    public void setFundingId(Long fundingId) {
        this.fundingId = fundingId;
    }

    public Long getInvestmentTotal() {
        return investmentTotal;
    }

    public void setInvestmentTotal(Long investmentTotal) {
        this.investmentTotal = investmentTotal;
    }
}
