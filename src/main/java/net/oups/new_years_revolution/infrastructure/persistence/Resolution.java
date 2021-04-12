package net.oups.new_years_revolution.infrastructure.persistence;


import javax.persistence.*;

@Entity
public class Resolution {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 500)
    private String contenu;

    private Integer occurence;

    private Integer periode; // Nombre de semaines pour réaliser une occurence (0 = tous les jours)

    @OneToOne
    private Account creator;

    public Resolution() {
        // JPA
    }

    public Resolution(String contenu, Integer occurence, Integer periode, Account creator) {
        this.contenu = contenu;
        this.occurence = occurence;
        this.periode = periode;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Integer getOccurence() {
        return occurence;
    }

    public void setOccurence(Integer occurence) {
        this.occurence = occurence;
    }

    public Integer getPeriode() {
        return periode;
    }

    public void setPeriode(Integer periode) {
        this.periode = periode;
    }

    public Account getCreator() {
        return creator;
    }

    public void setCreator(Account creator) {
        this.creator = creator;
    }
}