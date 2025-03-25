package asset.spy.auth.service.service;

import asset.spy.auth.service.entity.AccountEntity;
import asset.spy.auth.service.security.details.AccountDetails;
import asset.spy.auth.service.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Getting account");

        Optional<AccountEntity> account = accountRepository.findByLogin(username);

        if (account.isEmpty()) {
            throw new EntityNotFoundException("Account with login '" + username + "' not found");
        }

        return new AccountDetails(account.get());
    }
}
