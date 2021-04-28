package net.oups.new_years_revolution.infrastructure.persistence;


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
    private Date dateAbonnement;

    public Inscription() {
        // JPA
    }

    public Inscription(Account inscrit, Resolution resolution, Date dateAbonnement) {
        this.inscrit = inscrit;
        this.resolution = resolution;
        this.dateAbonnement = dateAbonnement;
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

    public Date getDateAbonnement() {
        return dateAbonnement;
    }

    public void setDateAbonnement(Date dateInscription) {
        this.dateAbonnement = dateInscription;
    }
}