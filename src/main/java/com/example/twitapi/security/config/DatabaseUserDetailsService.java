package com.example.twitapi.security.config;

import com.example.twitapi.security.model.SecurityUser;
import com.example.twitapi.security.repository.SecurityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private SecurityUserRepository securityUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SecurityUser> userModel = securityUserRepository.findUserModelByUsername(username);
        return userModel.map(UserModelUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User [%s] not found", username)));
    }
}
