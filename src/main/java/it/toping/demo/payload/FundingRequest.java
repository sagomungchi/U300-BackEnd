package it.toping.demo.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class FundingRequest {

    @NotBlank
    @Size(max = 40)
    private int balance;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
