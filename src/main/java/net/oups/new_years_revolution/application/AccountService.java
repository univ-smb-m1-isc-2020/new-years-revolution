package net.oups.new_years_revolution.application;

import net.oups.new_years_revolution.infrastructure.persistence.Account;
import net.oups.new_years_revolution.infrastructure.persistence.AccountRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AccountService {
    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account getAccountByLogin(String login) throws Exception {
        Account user = repository.findByLogin(login);
        if (user == null) {
            throw new Exception("User \"" + login + "\" does not exist.");
        }
        return user;
    }
}
