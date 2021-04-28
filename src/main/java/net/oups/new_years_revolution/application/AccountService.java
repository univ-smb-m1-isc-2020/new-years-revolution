package net.oups.new_years_revolution.application;

import net.oups.new_years_revolution.infrastructure.exceptions.AccountAlreadyExistsException;
import net.oups.new_years_revolution.infrastructure.exceptions.AccountDoesNotExistException;
import net.oups.new_years_revolution.infrastructure.exceptions.AccountPasswordNotMatchException;
import net.oups.new_years_revolution.infrastructure.persistence.Account;
import net.oups.new_years_revolution.infrastructure.dto.AccountDTO;
import net.oups.new_years_revolution.infrastructure.persistence.AccountRepository;
import net.oups.new_years_revolution.infrastructure.persistence.AccountRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository repository;

    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account getAccountByLogin(String login) throws AccountDoesNotExistException {
        Account user = repository.findByLogin(login);
        if (user == null) {
            throw new AccountDoesNotExistException("User \"" + login + "\" does not exist.");
        }
        return user;
    }

    public Account registerNewAccountUser(AccountDTO accountDTO) throws AccountAlreadyExistsException, AccountPasswordNotMatchException {
        if (repository.findByLogin(accountDTO.getLogin()) != null) {
            throw new AccountAlreadyExistsException("User \"" + accountDTO.getLogin() + "\" already exists.");
        }
        if (!accountDTO.getPassword().matches(accountDTO.getPasswordMatch())) {
            throw new AccountPasswordNotMatchException("Passwords do not match.");
        }

        // New account is considered valid
        Account newAcc = new Account(accountDTO.getLogin(), passwordEncoder.encode(accountDTO.getPassword()), AccountRole.ROLE_USER, new Date());
        repository.saveAndFlush(newAcc);

        return newAcc;
    }

    public Account changePassword(Account account, AccountDTO accountDTO) throws AccountPasswordNotMatchException {
        if (!accountDTO.getPassword().equals(accountDTO.getPasswordMatch()))
            throw new AccountPasswordNotMatchException("Passwords do not match.");

        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        repository.save(account);
        return account;
    }

    public int getAccountCount() {
        return repository.findAll().size();
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }
}
