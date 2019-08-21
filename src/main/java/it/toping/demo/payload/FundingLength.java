package it.toping.demo.payload;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class FundingLength {

    @NotNull
    @Max(7)
    private Integer dates;

    @NotNull
    @Max(23)
    private Integer hours;

    public Integer getDates() {
        return dates;
    }

    public void setDates(Integer dates) {
        this.dates = dates;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }
}
