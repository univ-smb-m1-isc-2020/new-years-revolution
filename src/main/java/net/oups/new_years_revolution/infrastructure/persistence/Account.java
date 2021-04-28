package net.oups.new_years_revolution.infrastructure.persistence;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String login;

    private String password;

    private AccountRole role;

    @Temporal(TemporalType.DATE)
    private Date dateInscription;

    public Account() {
        // JPA
    }

    public Account(String login, String password, AccountRole role, Date dateInscription) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.dateInscription = dateInscription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String username) {
        this.login = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }
}
