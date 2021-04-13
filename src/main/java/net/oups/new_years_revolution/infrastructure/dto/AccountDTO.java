package net.oups.new_years_revolution.infrastructure.dto;

import com.sun.istack.NotNull;

public class AccountDTO {

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
    private String passwordMatch;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordMatch() {
        return passwordMatch;
    }

    public void setPasswordMatch(String passwordMatch) {
        this.passwordMatch = passwordMatch;
    }
}
