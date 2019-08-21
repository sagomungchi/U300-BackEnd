package it.toping.demo.payload;

public class FundingResponse {

    private long id;
    private int balance;
    private long investmentCnt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public long getInvestmentCnt() {
        return investmentCnt;
    }

    public void setInvestmentCnt(long investmentCnt) {
        this.investmentCnt = investmentCnt;
    }
}
