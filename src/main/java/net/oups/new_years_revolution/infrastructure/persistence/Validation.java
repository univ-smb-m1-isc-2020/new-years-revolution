package net.oups.new_years_revolution.infrastructure.persistence;


import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Validation {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Account creator;

    @OneToOne
    private Resolution resolution;

    @Temporal(TemporalType.DATE)
    private Date date;

    @NotNull
    private Integer occurence;


    public Validation() {
        // JPA
    }

    public Validation(Account creator, Resolution resolution, Date date, Integer occurence) {
        this.creator = creator;
        this.resolution = resolution;
        this.date = date;
        this.occurence = occurence;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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