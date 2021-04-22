package net.oups.new_years_revolution.infrastructure.persistence;


import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(ValidationCompositeKey.class)
public class Validation {

    @Id
    @OneToOne
    private Resolution resolution;

    @Id
    @OneToOne
    private Account creator;

    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    @NotNull
    private Integer occurence;


    public Validation() {
        // JPA
    }

    public Validation(Resolution resolution, Account creator, Date date, Integer occurence) {
        this.resolution = resolution;
        this.creator = creator;
        this.date = date;
        this.occurence = occurence;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public Account getCreator() {
        return creator;
    }

    public void setCreator(Account creator) {
        this.creator = creator;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getOccurence() {
        return occurence;
    }

    public void setOccurence(Integer occurence) {
        this.occurence = occurence;
    }
}