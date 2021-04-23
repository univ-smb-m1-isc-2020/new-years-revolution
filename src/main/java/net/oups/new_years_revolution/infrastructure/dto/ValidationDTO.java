package net.oups.new_years_revolution.infrastructure.dto;

import com.sun.istack.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ValidationDTO {

    @NotNull
    private Integer occurence;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public Integer getOccurence() {
        return occurence;
    }

    public void setOccurence(Integer occurence) {
        this.occurence = occurence;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
