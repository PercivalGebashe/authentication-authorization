package io.github.PercivalGebashe.authentication_authorization.service;

import io.github.PercivalGebashe.authentication_authorization.entity.UserLoginDetails;
import io.github.PercivalGebashe.authentication_authorization.repository.UserLoginDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserLoginDetailsService implements UserDetailsService {

    private final UserLoginDetailsRepository loginDetailsRepository;

    @Autowired
    public UserLoginDetailsService(UserLoginDetailsRepository loginDetailsRepository) {
        this.loginDetailsRepository = loginDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserLoginDetails> user = loginDetailsRepository.findByEmailAddress(username);

        if(user.isPresent()){
            var userDetails = user.get();
            return User.builder()
                .username(userDetails.getEmailAddress())
                .password(userDetails.getPasswordHash())
                .build();
        }else {
            throw  new UsernameNotFoundException(String.format("User with email %s not found.", username));
        }
    }
}
