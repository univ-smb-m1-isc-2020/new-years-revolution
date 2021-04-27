package net.oups.new_years_revolution.infrastructure.persistence;


import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Inscription {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Account inscrit;

    @OneToOne
    private Resolution resolution;

    @Temporal(TemporalType.DATE)
    private Date dateInscription;

    public Inscription() {
        // JPA
    }

    public Inscription(Account inscrit, Resolution resolution, Date dateInscription) {
        this.inscrit = inscrit;
        this.resolution = resolution;
        this.dateInscription = dateInscription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getInscrit() {
        return inscrit;
    }

    public void setInscrit(Account inscrit) {
        this.inscrit = inscrit;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }
}