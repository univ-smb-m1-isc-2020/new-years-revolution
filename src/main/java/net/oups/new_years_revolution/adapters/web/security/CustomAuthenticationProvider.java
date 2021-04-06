package net.oups.new_years_revolution.adapters.web.security;

import net.oups.new_years_revolution.infrastructure.persistence.Account;
import net.oups.new_years_revolution.infrastructure.persistence.AccountRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private AccountRepository accountRepository;

    public CustomAuthenticationProvider(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials().toString();

        Account acc = accountRepository.findByLogin(username);

        if (acc != null /* Account exists */
                && acc.getLogin().equals(username) /* Username match */
                && new BCryptPasswordEncoder().matches(password, acc.getPassword()) /* Password match */
        ) {
            /* Give expected authority */
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            SimpleGrantedAuthority authGiven = new SimpleGrantedAuthority(acc.getRole().toUpperCase(Locale.ROOT));
            authorities.add(authGiven);
            return new UsernamePasswordAuthenticationToken
                    (username, password, authorities);
        } else {
            throw new
                    BadCredentialsException("External system authentication failed - Incorrect username or password");
        }
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}